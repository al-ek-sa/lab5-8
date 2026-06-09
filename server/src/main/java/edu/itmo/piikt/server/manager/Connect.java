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

public record Connect(Dispatcher dispatcher, User command) {
	private static final AppLogger logger = new AppLogger(Connect.class);
	private static final ExecutorService responsePool = Executors.newVirtualThreadPerTaskExecutor();
	private static final int HEADER_SIZE = 8;

	public void connected(SelectionKey selectionKey) throws IOException {
		try (Context ignored = Context.newId()) {
			var serverChannel = (ServerSocketChannel) selectionKey.channel();
			var clientChannel = serverChannel.accept();
			clientChannel.configureBlocking(false);
			logger.info("New client connected: {}", clientChannel.getRemoteAddress());
			ClientData client = new ClientData(65536);
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

		int read;
		try {
			read = clientChannel.read(buffer);
		} catch (IOException e) {
			logger.warn("Client disconnected: {}", e.getMessage());
			clientChannel.close();
			return;
		}

		if (read == -1) {
			logger.info("Client disconnected: {}", clientChannel.getRemoteAddress());
			clientChannel.close();
			return;
		}

		if (read == 0)
			return;

		if (client.isReadingChunked()) {
			buffer.flip();
			byte[] chunk = new byte[buffer.remaining()];
			buffer.get(chunk);
			client.addChunk(chunk);
			client.clearReader();

			if (client.isChunkedComplete()) {
				byte[] fullData = client.getAssembledData();
				client.finishChunkedReading();
				processCommand(selectionKey, clientChannel, client, fullData);
			} else {
				selectionKey.interestOps(SelectionKey.OP_READ);
				selectionKey.selector().wakeup();
			}
			return;
		}

		if (buffer.position() < HEADER_SIZE) {
			selectionKey.interestOps(SelectionKey.OP_READ);
			selectionKey.selector().wakeup();
			return;
		}

		buffer.flip();
		long messageSize = buffer.getLong();
		int dataSize = (int) messageSize;

		if (dataSize > buffer.remaining()) {
			client.startChunkedReading(messageSize);
			byte[] firstChunk = new byte[buffer.remaining()];
			buffer.get(firstChunk);
			client.addChunk(firstChunk);
			client.clearReader();
			selectionKey.interestOps(SelectionKey.OP_READ);
			selectionKey.selector().wakeup();
			return;
		}

		byte[] data = new byte[dataSize];
		buffer.get(data);
		client.clearReader();
		processCommand(selectionKey, clientChannel, client, data);
	}

	private void processCommand(SelectionKey selectionKey, SocketChannel clientChannel, ClientData client,
			byte[] data) {
		Thread.ofVirtual().start(() -> {
			try {
				ClientCommand clientCommand = DS.deserialize(ByteBuffer.wrap(data), ClientCommand.class);
				client.setCommand(clientCommand);
				logger.debug("Received command: {}", clientCommand.nameCommand());

				ServerResponse serverResponse = dispatcher.dispatcher(clientCommand);
				if (serverResponse == null) {
					serverResponse = command.execute(clientCommand);
				}
				if (serverResponse == null) {
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
	}

	public void writer(SelectionKey selectionKey) {
		var clientChannel = (SocketChannel) selectionKey.channel();
		var client = (ClientData) selectionKey.attachment();
		var serverResponse = (ServerResponse) client.getMessage();
		if (serverResponse == null) {
			selectionKey.interestOps(SelectionKey.OP_READ);
			selectionKey.selector().wakeup();
			return;
		}

		ByteBuffer responseBuffer = DS.serializeWithSize(serverResponse);

		responsePool.submit(() -> {
			try {
				clientChannel.write(responseBuffer);
				logger.debug("Response sent");
				client.setMessage(null);
				client.clearReader();
			} catch (IOException e) {
				logger.error("Error sending response: {}", e.getMessage());
				try {
					clientChannel.close();
				} catch (IOException ignored) {
				}
			}
		});

		selectionKey.interestOps(SelectionKey.OP_READ);
		selectionKey.selector().wakeup();
	}
}
