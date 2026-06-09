package edu.itmo.piikt.server.manager;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.server.dispatcher.Dispatcher;
import edu.itmo.piikt.server.registration.User;
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
public class Network {
	private static final AppLogger logger = new AppLogger(Network.class);
	private static final int PORT = 6654;

	private final Dispatcher dispatcher;
	private Selector selector;
	private ServerSocketChannel serverSocketChannel;
	private boolean run = true;
	private final Connect connect;
	private final User user;

	public Network(Dispatcher dispatcher, User user) {
		this.dispatcher = dispatcher;
		this.user = user;
		this.connect = new Connect(dispatcher, user);
	}

	public void start() throws IOException {
		try (Context ignored = Context.newId()) {
			logger.info("Starting server on port {}", PORT);
			selector = Selector.open();
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.bind(new InetSocketAddress(PORT));
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			logger.info("Server started");

			while (run) {
				selector.select();
				Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
				while (keys.hasNext()) {
					SelectionKey key = keys.next();
					keys.remove();

					if (!key.isValid())
						continue;

					try {
						if (key.isAcceptable())
							connect.connected(key);
						else if (key.isReadable())
							connect.reader(key);
						else if (key.isWritable())
							connect.writer(key);
					} catch (Exception e) {
						logger.error("Error processing key: {}", e.getMessage());
						try {
							key.channel().close();
						} catch (IOException ignored1) {
						}
					}
				}
			}
			close();
		}
	}

	public void stop() {
		run = false;
		if (selector != null)
			selector.wakeup();
	}

	private void close() {
		try {
			if (selector != null)
				selector.close();
			if (serverSocketChannel != null)
				serverSocketChannel.close();
			logger.info("Server stopped");
		} catch (IOException e) {
			logger.error("Error closing server: {}", e.getMessage());
		}
	}
}
