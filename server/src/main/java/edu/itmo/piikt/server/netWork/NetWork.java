package edu.itmo.piikt.server.netWork;

import edu.itmo.piikt.server.dispatcher.Dispatcher;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

@Data
@AllArgsConstructor
public class NetWork {
    private static  final int PORT = 6668;
    private final Dispatcher dispatcher;
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private  boolean run = true;
    private  final Connect connect;

    public NetWork(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
        this.connect = new Connect(dispatcher);
    }
    public void start() throws IOException{
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(PORT));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (run) {
            selector.select();
            Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();
            while (selectionKeyIterator.hasNext()) {
                SelectionKey key = selectionKeyIterator.next();
                selectionKeyIterator.remove();
                if(!key.isValid()) {
                    continue;
                }
                try {
                    if (key.isAcceptable()) {
                        connect.connected(key);
                    } else if (key.isReadable()) {
                        connect.reader(key);
                    } else if (key.isWritable()) {
                        connect.writer(key);
                    }
                } catch (RuntimeException e) {
                    try {
                        key.channel().close();
                    } catch (IOException ex) {
                        e.getMessage();
                    }
                }
            }
        }
        close();
    }

    public void stop() {
        run = false;
        selector.wakeup();
    }
    private void close() {
        try {
            if (selector != null) {
                selector.close();
            }
            if (serverSocketChannel != null) {
                serverSocketChannel.close();
            }
        } catch (IOException e) {
            e.getMessage();
        }
    }
}
