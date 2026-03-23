package edu.itmo.piikt.server.netWork;

import edu.itmo.piikt.common.server_client.ClientCommand;
import edu.itmo.piikt.common.server_client.ServerResponse;
import edu.itmo.piikt.server.command.modelCommand.ExitCommand;
import edu.itmo.piikt.server.dispatcher.Dispatcher;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

@Data
public class Connect {
    private final Dispatcher dispatcher;

    public Connect(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public void connected(SelectionKey selectionKey) throws IOException {
        var serverChannel = (ServerSocketChannel) selectionKey.channel();
        var clientChanel = serverChannel.accept();
        clientChanel.configureBlocking(false);
        ClientData client = new ClientData(6666);
        clientChanel.register(selectionKey.selector(), SelectionKey.OP_READ, client);
    }

    public void reader(SelectionKey selectionKey) throws IOException {
        var clientChanel = (SocketChannel) selectionKey.channel();
        var client = (ClientData) selectionKey.attachment();
        var buffer = client.getReader();
        var reader = clientChanel.read(buffer);
        if (reader == -1) {
            clientChanel.close();
            return;
        }
        if (reader == 0) {
            return;
        }

        buffer.flip();
        try {
            ClientCommand clientCommand = (ClientCommand) DS.deserialize(buffer);
            client.setCommand(clientCommand);
            ServerResponse serverResponse = dispatcher.dispatcher(clientCommand);
            client.setMessage(serverResponse);
            selectionKey.interestOps(SelectionKey.OP_WRITE);
        } catch (Exception e) {
            client.clearReader();
        }
    }

    public void writer (SelectionKey selectionKey) throws IOException {
        var clientChanel = (SocketChannel) selectionKey.channel();
        var client = (ClientData) selectionKey.attachment();
        var serverResponse = (ServerResponse) client.getMessage();
        try {
            ByteBuffer byteBuffer = DS.serialize(serverResponse);
            clientChanel.write(byteBuffer);
            client.setMessage(null);
            client.clearReader();
            selectionKey.interestOps(SelectionKey.OP_READ);
        } catch (Exception e) {
            selectionKey.interestOps(SelectionKey.OP_READ);
        }
    }
}
