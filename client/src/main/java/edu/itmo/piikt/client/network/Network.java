package edu.itmo.piikt.client.network;

import edu.itmo.piikt.common.interfaceCommon.Client;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.protocol.Frame;
import edu.itmo.piikt.common.protocol.Message;
import edu.itmo.piikt.common.protocol.MessageBuffer;
import edu.itmo.piikt.common.protocol.ProtocolMessage;
import edu.itmo.piikt.common.server_client.ClientCommand;
import edu.itmo.piikt.common.server_client.ServerResponse;
import edu.itmo.piikt.common.util.DS;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Network implements Client {
    private static final Integer SIZE = 66666;
    private static final int MAX = 5;
    private static final Integer TIME = 3000;
    private static final AppLogger logger = new AppLogger(Network.class);

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class PendingRequest {
        private ProtocolMessage request;
        private long sentTime;
        private int retries;
        private Object response;
    }

    private SocketChannel socketChannel;
    private static final String HOST = System.getenv().getOrDefault("SERVER_HOST", "localhost");
    private final Integer PORT = 6668;
    private final MessageBuffer messageBuffer = new MessageBuffer(SIZE);
    private final Map<UUID, PendingRequest> pendingRequestMap = new ConcurrentHashMap<>();

    @Override
    public void connect() throws IOException {
        connectWithRetry(MAX);
    }

    private void connectWithRetry(int number) throws IOException{
        try (Context ignored = Context.newId()) {
            logger.info("Connecting to {}:{}", HOST, PORT);
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress(HOST, PORT));
            while (!socketChannel.finishConnect()) {
                Thread.yield();
            }
            logger.info("Connected successfully");
        }catch (ConnectException e){
            if (number > 1) {
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    throw new IOException();
                }
                connectWithRetry(number -1);
            } else {
                throw new IOException();
            }
        } catch (IOException e) {
            if (number > 1) {
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    throw new IOException();
                }
                connectWithRetry(number -1);
            } else {
                throw e;
            }
        }
    }

    private void readIncoming() throws IOException{
        if (socketChannel == null || !socketChannel.isConnected()){
            return;
        }
        ByteBuffer buffer = ByteBuffer.allocate(SIZE);
        int bytesRead = socketChannel.read(buffer);

        if (bytesRead > 0) {
            buffer.flip();
            messageBuffer.append(buffer);

            List<byte[]> messages = messageBuffer.extract();
            for (byte[] msg : messages) {
                ProtocolMessage response = (ProtocolMessage) DS.deserialize(ByteBuffer.wrap(msg));
                handleResponse(response);
            }
        } else if (bytesRead == -1){
            close();
            connect();
        }
    }

    private void handleResponse(ProtocolMessage response) {
        UUID id = response.getId();
        PendingRequest pending = pendingRequestMap.get(id);

        if (pending != null && response.getType() == Message.SERVER_RESPONSE) {
            pending.setResponse(response.getData());
        }
    }

    @Override
    public ServerResponse send(ClientCommand clientCommand) throws Exception {
        try (Context ignored = Context.newId()) {
            if (socketChannel == null || !socketChannel.isConnected()){
                connect();
            }
            logger.debug("Sending command: {}", clientCommand.getNameCommand());
            UUID id = UUID.randomUUID();
            ProtocolMessage message = ProtocolMessage.builder()
                    .id(id)
                    .type(Message.CUSTOMER_REQUEST)
                    .data(clientCommand)
                    .time(System.currentTimeMillis())
                    .build();
            ByteBuffer serialized = DS.serialize(message);
            byte[] data = new byte[serialized.remaining()];
            serialized.get(data);
            ByteBuffer framed = Frame.encode(data);
            pendingRequestMap.put(id, new PendingRequest(message, System.currentTimeMillis(), 0, null));
            socketChannel.write(framed);
            ServerResponse result = null;
            while (result == null) {
                readIncoming();
                PendingRequest pending = pendingRequestMap.get(id);
                if (pending != null && pending.getResponse() != null) {
                    result = (ServerResponse) pending.getResponse();
                    pendingRequestMap.remove(id);
                    break;
                }
                if (pending != null && System.currentTimeMillis() - pending.getSentTime() > TIME) {
                    if (pending.getRetries() >= MAX) {
                        pendingRequestMap.remove(id);
                        throw new IOException("No response from server");
                    }
                    pending.setRetries(pending.getRetries() + 1);
                    pending.setSentTime(System.currentTimeMillis());
                    ByteBuffer resendSerialized = DS.serialize(message);
                    byte[] resendData = new byte[resendSerialized.remaining()];
                    resendSerialized.get(resendData);
                    ByteBuffer resendFramed = Frame.encode(resendData);
                    socketChannel.write(resendFramed);
                    logger.debug("Resent request: {}, retry {}", id, pending.getRetries());
                }
                Thread.sleep(1500);
            }
            return result;
        } catch (Exception e) {
            logger.error("Error sending command: {}", e);
            throw e;
        }
    }

    @Override
    public boolean connected() {
        boolean isConnected = socketChannel != null && socketChannel.isConnected();
        logger.debug("Connection status: {}", isConnected);
        return isConnected;
    }

    @Override
    public void close() throws IOException {
        try (Context ignored = Context.newId()) {
            logger.info("Closing connection");
            if (socketChannel != null) {
                socketChannel.close();
            }
            logger.info("Connection closed");
        } catch (IOException e) {
            logger.error("Error closing connection: {}", e);
            throw e;
        }
    }
}