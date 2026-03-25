package edu.itmo.piikt.server.netWork;

import edu.itmo.piikt.server.CommandServer.CommandFactory;
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
    private static  final int PORT = 6672;
    private final Dispatcher dispatcher;
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private  boolean run = true;
    private  final Connect connect;
    private CommandFactory commandFactory;
    private final StringBuilder stringBuilder = new StringBuilder();

    public NetWork(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
        this.connect = new Connect(dispatcher);
        this.commandFactory = new CommandFactory();
    }

    private void console()  {
        try{
            if (System.in.available() > 0) {
                while (System.in.available() > 0) {
                    char c = (char) System.in.read();
                    stringBuilder.append(c);
                    if (c == '\n') {
                        String command = stringBuilder.toString().trim();
                        stringBuilder.setLength(0);
                        if (!command.isEmpty()){
                        commandFactory.execute(command);}
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void start() throws IOException{
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(new InetSocketAddress(PORT));
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (run) {
            console();
            selector.select(1000);
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
