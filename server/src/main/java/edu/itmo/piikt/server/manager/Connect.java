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

/**
 * Handles client connections, reading commands, and sending responses
 *
 * @param dispatcher
 *            command dispatcher for processing client commands
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
public record Connect(Dispatcher dispatcher, User command) {
	/** Return value indicating end of stream */
	private static final int END_OF_STREAM = -1;
	/** Return value indicating no data available to read */
	private static final int NO_DATA_READ = 0;
	private static final AppLogger logger = new AppLogger(Connect.class);

	/**
	 * Handles new client connection
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
	 * Reads data from client
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
		var reader = clientChannel.read(buffer);

		// Client disconnected
		if (reader == END_OF_STREAM) {
			logger.info("Client disconnected: {}", clientChannel.getRemoteAddress());
			clientChannel.close();
			return;
		}
		// No data available
		if (reader == NO_DATA_READ) {
			return;
		}

		// Process the received command
		buffer.flip();
		try (Context ignored = Context.newId()) {
			ClientCommand clientCommand = (ClientCommand) DS.deserialize(buffer);
			client.setCommand(clientCommand);
			logger.debug("Received command from {}: {}", clientChannel.getRemoteAddress(),
					clientCommand.getNameCommand());
			ServerResponse serverResponse = dispatcher.dispatcher(clientCommand);
			if (serverResponse == null) {
				serverResponse = command.execute(clientCommand);
			}
			client.setMessage(serverResponse);
			selectionKey.interestOps(SelectionKey.OP_WRITE);
		} catch (Exception e) {
			logger.error("Error processing command: {}", e.getMessage());
			client.clearReader();
		}
	}

	/**
	 * Sends response to client
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

		try (Context ignored = Context.newId()) {
			ByteBuffer byteBuffer = DS.serialize(serverResponse);
			clientChannel.write(byteBuffer);
			logger.debug("Response sent to {}", clientChannel.getRemoteAddress());
			client.setMessage(null);
			client.clearReader();
			selectionKey.interestOps(SelectionKey.OP_READ);
		} catch (Exception e) {
			logger.error("Error sending response: {}", e.getMessage());
			selectionKey.interestOps(SelectionKey.OP_READ);
		}
	}
}
