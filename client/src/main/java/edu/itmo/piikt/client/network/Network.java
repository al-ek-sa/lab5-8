package edu.itmo.piikt.client.network;

import edu.itmo.piikt.common.interfaceCommon.Client;
import edu.itmo.piikt.common.server_client.ClientCommand;
import edu.itmo.piikt.common.server_client.ServerResponse;
import edu.itmo.piikt.common.util.DS;
import edu.itmo.piikt.common.server_client.ClientData;
import lombok.Data;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

@Data
public class Network implements Client {
    private static final Integer SIZE = 66666;
    private static final Integer TIME = 3000;
    private SocketChannel socketChannel;
    private final String host;
    private final Integer PORT = 6672;
    private ClientData clientData;
    public Network (String host) {
        this.host = host;
    }
    @Override
    public void connect() throws IOException {
        socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(true);
        socketChannel.connect(new InetSocketAddress(host, PORT));
        clientData = new ClientData(SIZE);
    }

    @Override
    public ServerResponse send(ClientCommand clientResponse) throws Exception {
        ByteBuffer writer = DS.serialize(clientResponse);
        socketChannel.write(writer);
        socketChannel.socket().setSoTimeout(TIME);
        ByteBuffer reader = clientData.getReader();
        reader.clear();
        Integer bytes = socketChannel.read(reader);
        if (bytes == -1) {
            throw  new IOException("Соединение закрыто");
        }
        reader.flip();
        ServerResponse serverResponse = (ServerResponse) DS.deserialize(reader);
        return serverResponse;
    }

    @Override
    public boolean connected() {
        return socketChannel != null && socketChannel.isConnected();
    }

    @Override
    public void close() throws IOException {
        if (socketChannel != null) {
            socketChannel.close();
        }
    }
}
