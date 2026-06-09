package edu.itmo.piikt.client.network;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.sc.Client;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;
import edu.itmo.piikt.common.util.DS;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.SocketChannel;

import static java.lang.Thread.*;

@Data
@NoArgsConstructor
public class Network implements Client {
	private static final AppLogger logger = new AppLogger(Network.class);
	private static final int HEADER_SIZE = 8;
	private static final int BUFFER_SIZE = 65536;
	private static final int SO_TIMEOUT = 60000;
	private static final String HOST = System.getenv().getOrDefault("SERVER_HOST", "localhost");
	private static final int PORT = 6654;

	private SocketChannel socketChannel;
	private volatile boolean isClosing = false;

	@Override
	public void connect() {
		connectWithRetry();
	}

	private void connectWithRetry() {
		while (!currentThread().isInterrupted() && !isClosing) {
			try (Context ignored = Context.newId()) {
				logger.info("Connecting to {}:{}", HOST, PORT);
				socketChannel = SocketChannel.open();
				socketChannel.configureBlocking(true);
				socketChannel.socket().setSoTimeout(SO_TIMEOUT);
				socketChannel.connect(new InetSocketAddress(HOST, PORT));
				logger.info("Connected successfully");
				return;
			} catch (IOException e) {
				if (!isClosing) {
					logger.warn("Connection failed, retrying...");
				}
				try {
					sleep(1000);
				} catch (InterruptedException ex) {
					currentThread().interrupt();
					return;
				}
			}
		}
	}

	@Override
	public ServerResponse send(ClientCommand clientResponse) throws Exception {
		if (isClosing) {
			throw new IOException("Connection is closing");
		}

		try (Context ignored = Context.newId()) {
			if (socketChannel == null || !socketChannel.isConnected()) {
				connect();
			}
			logger.debug("Sending command: {}", clientResponse.nameCommand());
			ByteBuffer writeBuffer = DS.serializeWithSize(clientResponse);
			socketChannel.write(writeBuffer);
			ByteBuffer headerBuffer = ByteBuffer.allocate(HEADER_SIZE);
			int headerBytes = 0;
			while (headerBytes < HEADER_SIZE) {
				int read = socketChannel.read(headerBuffer);
				if (read == -1) {
					throw new IOException("Connection closed while reading header");
				}
				headerBytes += read;
			}
			headerBuffer.flip();
			long responseSize = headerBuffer.getLong();

			logger.debug("Expecting response of size: {} bytes", responseSize);
			ByteBuffer dataBuffer = ByteBuffer.allocate((int) responseSize);
			int dataBytes = 0;
			while (dataBytes < responseSize) {
				int read = socketChannel.read(dataBuffer);
				if (read == -1) {
					throw new IOException("Connection closed while reading data");
				}
				dataBytes += read;
			}
			dataBuffer.flip();
			ServerResponse response = DS.deserialize(dataBuffer, ServerResponse.class);
			logger.debug("Response received: success={}", response.execution());
			return response;

		} catch (ClosedByInterruptException e) {
			Thread.currentThread().interrupt();
			throw new IOException("Connection interrupted", e);
		} catch (Exception e) {
			if (!isClosing && !Thread.currentThread().isInterrupted()) {
				logger.error("Error sending command: {}", e.getMessage(), e);
			}
			throw e;
		}
	}

	@Override
	public void close() throws IOException {
		isClosing = true;
		try (Context ignored = Context.newId()) {
			logger.info("Closing connection");
			if (socketChannel != null) {
				try {
					socketChannel.close();
				} catch (IOException e) {
					logger.debug("Error closing socket: {}", e.getMessage());
				}
			}
			logger.info("Connection closed");
		}
	}

	public boolean connected() {
		return socketChannel != null && socketChannel.isConnected() && socketChannel.isOpen();
	}
}
