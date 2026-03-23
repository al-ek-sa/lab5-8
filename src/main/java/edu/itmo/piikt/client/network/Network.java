package edu.itmo.piikt.client.network;

import edu.itmo.piikt.common.server_client.ClientCommand;
import edu.itmo.piikt.common.server_client.ServerResponse;
import edu.itmo.piikt.common.util.DS;
import lombok.Data;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

@Data
public class Network {
    private static final Integer SIZE = 6666;
    private static final Integer TIME = 3000;
    private SocketChannel socketChannel;
    private final String host;
    private final Integer PORT = 6666;
    private ClientData clientData;
    public Network (String host) {
        this.host = host;
    }

    public void connect() throws IOException {
        socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(true);
        socketChannel.connect(new InetSocketAddress(host, PORT));
        clientData = new ClientData(SIZE);
    }
    //todo переименовать
    //todo ошибки
    public ServerResponse aaaa(ClientCommand clientCommand) throws Exception {
        ByteBuffer writer = DS.serialize(clientCommand);
        socketChannel.write(writer);
        socketChannel.socket().setSoTimeout(TIME);
        ByteBuffer reader = clientData.getReader();
        reader.clear();
        Integer bytes = socketChannel.read(reader);
        if (bytes == -1) {
            throw  new IOException("");
        }
        reader.flip();
        ServerResponse serverResponse = (ServerResponse) DS.deserialize(reader);
        return serverResponse;
    }

    public boolean connected() {
        return socketChannel != null && socketChannel.isConnected();
    }

    public void close() throws IOException {
        if (socketChannel != null) {
            socketChannel.close();
        }
    }
}
