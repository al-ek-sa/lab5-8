package edu.itmo.piikt.server.netWork;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.server.CommandServer.CommandFactory;
import edu.itmo.piikt.server.dispatcher.Dispatcher;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Main network server class for handling client connections
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@Data
@AllArgsConstructor
public class NetWork {
	/** Server port number */
	private static final int PORT = 6969;
	/** No data available for console input */
	private static final int NO_DATA = 0;
	/** Selector timeout in milliseconds */
	private static final int TIME = 5;
	/** Maximum bytes to read from console per iteration (1KB chunk) */
	private static final int MAX_CONSOLE = 1024;
	private static final AppLogger logger = new AppLogger(NetWork.class);
	private final Dispatcher dispatcher;
	private Selector selector;
	private ServerSocketChannel serverSocketChannel;
	private boolean run = true;
	private final Connect connect;
	private CommandFactory commandFactory;
	private final StringBuilder stringBuilder = new StringBuilder();

	public NetWork(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		this.connect = new Connect(dispatcher);
		this.commandFactory = new CommandFactory();
	}

	/**
	 * Reads and processes console commands
	 */
	private void console() {
		try {
			int input = System.in.available();
			if (input > NO_DATA) {
				int bytes = Math.min(input, MAX_CONSOLE);
				for (int i = 0; i < bytes; i++) {
					char c = (char) System.in.read();
					stringBuilder.append(c);
					if (c == '\n') {
						String command = stringBuilder.toString().trim();
						stringBuilder.setLength(0);
						if (!command.isEmpty()) {
							commandFactory.execute(command);
						}
					}
				}
			}
		} catch (IOException e) {
			logger.error("Console input error: {}", e.getMessage());
		}
	}

	/**
	 * Starts the server and begins accepting client connections
	 *
	 * @throws IOException
	 *             if server fails to start
	 */
	public void start() throws IOException {
		try (Context ignored = Context.newId()) {
			logger.info("Starting server on port {}", PORT);
			selector = Selector.open();
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.bind(new InetSocketAddress(PORT));
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			logger.info("Server started successfully");

			while (run) {
				console();
				selector.select(TIME);
				Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();
				while (selectionKeyIterator.hasNext()) {
					SelectionKey key = selectionKeyIterator.next();
					selectionKeyIterator.remove();
					if (!key.isValid()) {
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
						logger.error("Error processing key: {}", e.getMessage());
						try {
							key.channel().close();
						} catch (IOException ex) {
							logger.error("Error closing channel: {}", ex.getMessage());
						}
					}
				}
			}
			close();
		} catch (IOException e) {
			logger.error("Server start failed: {}", e.getMessage());
			throw e;
		}
	}

	/**
	 * Stops the server gracefully
	 */
	public void stop() {
		try (Context ignored = Context.newId()) {
			logger.info("Stopping server");
			run = false;
			if (selector != null) {
				selector.wakeup();
			}
		}
	}

	/**
	 * Closes server resources
	 */
	private void close() {
		try {
			if (selector != null) {
				selector.close();
			}
			if (serverSocketChannel != null) {
				serverSocketChannel.close();
			}
			logger.info("Server stopped");
		} catch (IOException e) {
			logger.error("Error closing server: {}", e.getMessage());
		}
	}
}
