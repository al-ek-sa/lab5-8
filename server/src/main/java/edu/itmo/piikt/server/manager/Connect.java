package edu.itmo.piikt.server.manager;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ClientData;
import edu.itmo.piikt.common.sc.ServerResponse;
import edu.itmo.piikt.common.util.DS;
import edu.itmo.piikt.server.dispatcher.Dispatcher;
import edu.itmo.piikt.server.registration.User;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Handles client connections, reading commands, and sending responses.
 *
 * @param dispatcher
 *            command dispatcher for processing client commands
 * @param command
 *            authentication command router
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
public record Connect(Dispatcher dispatcher, User command) {
	/** Return value indicating end of stream */
	private static final int END_OF_STREAM = -1;
	/** Return value indicating no data available to read */
	private static final int NO_DATA_READ = 0;
	private static final AppLogger logger = new AppLogger(Connect.class);
	/** Fixed thread pool for sending responses */
	private static final ExecutorService responsePool = Executors.newFixedThreadPool(10);

	/**
	 * Handles new client connection.
	 *
	 * @param selectionKey
	 *            key containing the server channel
	 * @throws IOException
	 *             if accepting connection fails
	 */
	public void connected(SelectionKey selectionKey) throws IOException {
		try (Context ignored = Context.newId()) {
			var serverChannel = (ServerSocketChannel) selectionKey.channel();
			var clientChannel = serverChannel.accept();
			clientChannel.configureBlocking(false);
			logger.info("New client connected: {}", clientChannel.getRemoteAddress());
			ClientData client = new ClientData(66666);
			clientChannel.register(selectionKey.selector(), SelectionKey.OP_READ, client);
		} catch (IOException e) {
			logger.error("Error accepting connection: {}", e.getMessage());
			throw e;
		}
	}

	/**
	 * Reads data from client.
	 *
	 * @param selectionKey
	 *            key containing the client channel and attachment
	 * @throws IOException
	 *             if reading fails
	 */
	public void reader(SelectionKey selectionKey) throws IOException {
		var clientChannel = (SocketChannel) selectionKey.channel();
		var client = (ClientData) selectionKey.attachment();
		var buffer = client.getReader();
		int reader;
		try {
			reader = clientChannel.read(buffer);
		} catch (IOException e) {
			logger.warn("Client disconnected unexpectedly: {}", e.getMessage());
			clientChannel.close();
			return;
		}
		if (reader == END_OF_STREAM) {
			logger.info("Client disconnected: {}", clientChannel.getRemoteAddress());
			clientChannel.close();
			return;
		}
		if (reader == NO_DATA_READ) {
			return;
		}

		buffer.flip();
		byte[] data = new byte[buffer.remaining()];
		buffer.get(data);
		client.clearReader();

		Thread readThread = new Thread(() -> {
			try (Context ignored = Context.newId()) {
				ByteBuffer bb = ByteBuffer.wrap(data);
				ClientCommand clientCommand = (ClientCommand) DS.deserialize(bb);
				client.setCommand(clientCommand);
				logger.debug("Received command from {}: {}", clientChannel.getRemoteAddress(),
						clientCommand.getNameCommand());

				Thread processThread = new Thread(() -> {
					try (Context ignored2 = Context.newId()) {
						ServerResponse serverResponse = dispatcher.dispatcher(clientCommand);
						if (serverResponse == null) {
							serverResponse = command.execute(clientCommand);
						}
						if (serverResponse == null) {
							logger.error("Command returned null response for: {}", clientCommand.getNameCommand());
							serverResponse = ServerResponse.error("Internal server error");
						}
						client.setMessage(serverResponse);
						selectionKey.interestOps(SelectionKey.OP_WRITE);
						selectionKey.selector().wakeup();
					} catch (Exception e) {
						logger.error("Error processing command: {}", e.getMessage());
						client.clearReader();
						selectionKey.interestOps(SelectionKey.OP_READ);
						selectionKey.selector().wakeup();
					}
				});
				processThread.start();
			} catch (Exception e) {
				logger.error("Error deserializing command: {}", e.getMessage());
				client.clearReader();
				selectionKey.interestOps(SelectionKey.OP_READ);
				selectionKey.selector().wakeup();
			}
		});
		readThread.start();
	}

	/**
	 * Sends response to client.
	 *
	 * @param selectionKey
	 *            key containing the client channel and attachment
	 * @throws IOException
	 *             if writing fails
	 */
	public void writer(SelectionKey selectionKey) throws IOException {
		var clientChannel = (SocketChannel) selectionKey.channel();
		var client = (ClientData) selectionKey.attachment();
		var serverResponse = (ServerResponse) client.getMessage();

		if (serverResponse == null) {
			selectionKey.interestOps(SelectionKey.OP_READ);
			selectionKey.selector().wakeup();
			return;
		}
		final ByteBuffer responseBuffer = DS.serialize(serverResponse);
		final ClientData clientData = client;
		responsePool.submit(() -> {
			try (Context ignored = Context.newId()) {
				clientChannel.write(responseBuffer);
				logger.debug("Response sent to {}", clientChannel.getRemoteAddress());
				clientData.setMessage(null);
				clientData.clearReader();
			} catch (IOException e) {
				logger.error("Error sending response: {}", e.getMessage());
				try {
					clientChannel.close();
				} catch (IOException ex) {
					logger.error("Error closing channel: {}", ex.getMessage());
				}
			} catch (Exception e) {
				logger.error("Unexpected error sending response: {}", e.getMessage());
			}
		});
		selectionKey.interestOps(SelectionKey.OP_READ);
		selectionKey.selector().wakeup();
	}
}
