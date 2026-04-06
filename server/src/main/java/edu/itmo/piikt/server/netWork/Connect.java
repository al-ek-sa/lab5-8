package edu.itmo.piikt.server.netWork;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.protocol.Frame;
import edu.itmo.piikt.common.protocol.Message;
import edu.itmo.piikt.common.protocol.MessageBuffer;
import edu.itmo.piikt.common.protocol.ProtocolMessage;
import edu.itmo.piikt.common.server_client.ClientCommand;
import edu.itmo.piikt.common.server_client.ClientData;
import edu.itmo.piikt.common.server_client.ServerResponse;
import edu.itmo.piikt.common.util.DS;
import edu.itmo.piikt.server.dispatcher.Dispatcher;
import lombok.Data;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class Connect {
    private static final int SIZE = 666666;
    private static final AppLogger logger = new AppLogger(Connect.class);
    private final Dispatcher dispatcher;
    private final Map<SocketChannel, MessageBuffer> clientBuffer = new ConcurrentHashMap<>();
    private final Map<UUID, ServerResponse> map = new ConcurrentHashMap<>();

    public Connect(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    private void sendMessage(ProtocolMessage protocolMessage, SocketChannel socketChannel) throws IOException{
        ByteBuffer byteBuffer =DS.serialize(protocolMessage);
        byte[] data = new byte[byteBuffer.remaining()];
        byteBuffer.get(data);
        ByteBuffer framed = Frame.encode(data);
        socketChannel.write(framed);
    }

    private void send(UUID id, ServerResponse serverResponse, SocketChannel socketChannel) throws IOException {
        ProtocolMessage protocolMessage = ProtocolMessage.builder().
        id(id).type(Message.SERVER_RESPONSE).data(serverResponse).time(System.currentTimeMillis()).
        build();
        sendMessage(protocolMessage, socketChannel);
    }

    private void handle(ProtocolMessage protocolMessage, SocketChannel socketChannel) throws IOException {
        UUID id = protocolMessage.getId();
        ProtocolMessage ask = ProtocolMessage.builder().id(id).type(Message.ASK).time(System.currentTimeMillis()).build();
        sendMessage(ask, socketChannel);
        if (map.containsKey(id)) {
            send(id, map.get(id), socketChannel);
            return;
        }
        ClientCommand command = (ClientCommand) protocolMessage.getData();
        ServerResponse response = dispatcher.dispatcher(command);
        map.put(id, response);
        send(id, response, socketChannel);
    }

    public void connected(SelectionKey selectionKey) throws IOException {
        try (Context ignored = Context.newId()) {
            var serverChannel = (ServerSocketChannel) selectionKey.channel();
            var clientChannel = serverChannel.accept();
            clientChannel.configureBlocking(false);
            logger.info("New client connected: {}", clientChannel.getRemoteAddress());
            clientBuffer.put(clientChannel, new MessageBuffer(SIZE));
            clientChannel.register(selectionKey.selector(), SelectionKey.OP_READ);
        } catch (IOException e) {
            logger.error("Error accepting connection: {}", e.getMessage());
            throw e;
        }
    }

    public void reader(SelectionKey selectionKey) throws IOException {
        var clientChannel = (SocketChannel) selectionKey.channel();
        var buffer = clientBuffer.get(clientChannel);
        var readerBuffer = ByteBuffer.allocate(SIZE);
        int reader = clientChannel.read(readerBuffer);

        if (reader == -1) {
            logger.info("Client disconnected: {}", clientChannel.getRemoteAddress());
            clientBuffer.remove(clientChannel);
            clientChannel.close();
            return;
        }
        if (reader > 0) {
            readerBuffer.flip();
            buffer.append(readerBuffer);
            List<byte[]> message = buffer.extract();
            for (byte[] messag: message) {
                try{
                    ProtocolMessage protocolMessage = (ProtocolMessage) DS.deserialize(ByteBuffer.wrap(messag));
                    handle(protocolMessage, clientChannel);
                } catch (Exception e) {
                    logger.error("Error deserializing message: {}", e.getMessage());
                }
            }
        }

        selectionKey.interestOps(SelectionKey.OP_READ);
    }

    public void writer(SelectionKey selectionKey) throws IOException {
            selectionKey.interestOps(SelectionKey.OP_READ);

    }
}