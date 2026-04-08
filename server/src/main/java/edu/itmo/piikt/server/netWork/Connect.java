package edu.itmo.piikt.server.netWork;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.server_client.ClientCommand;
import edu.itmo.piikt.common.server_client.ClientData;
import edu.itmo.piikt.common.server_client.ServerResponse;
import edu.itmo.piikt.common.util.DS;
import edu.itmo.piikt.server.dispatcher.Dispatcher;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public record Connect(Dispatcher dispatcher) {
	private static final AppLogger logger = new AppLogger(Connect.class);

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

	public void reader(SelectionKey selectionKey) throws IOException {
		var clientChannel = (SocketChannel) selectionKey.channel();
		var client = (ClientData) selectionKey.attachment();
		var buffer = client.getReader();
		var reader = clientChannel.read(buffer);

		if (reader == -1) {
			logger.info("Client disconnected: {}", clientChannel.getRemoteAddress());
			clientChannel.close();
			return;
		}
		if (reader == 0) {
			return;
		}

		buffer.flip();
		try (Context ignored = Context.newId()) {
			ClientCommand clientCommand = (ClientCommand) DS.deserialize(buffer);
			client.setCommand(clientCommand);
			logger.debug("Received command from {}: {}", clientChannel.getRemoteAddress(),
					clientCommand.getNameCommand());
			ServerResponse serverResponse = dispatcher.dispatcher(clientCommand);
			client.setMessage(serverResponse);
			selectionKey.interestOps(SelectionKey.OP_WRITE);
		} catch (Exception e) {
			logger.error("Error processing command: {}", e.getMessage());
			client.clearReader();
		}
	}

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
