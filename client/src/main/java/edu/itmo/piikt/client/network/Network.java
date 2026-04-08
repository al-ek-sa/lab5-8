package edu.itmo.piikt.client.network;

import edu.itmo.piikt.common.interfaceCommon.Client;
import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.server_client.ClientCommand;
import edu.itmo.piikt.common.server_client.ClientData;
import edu.itmo.piikt.common.server_client.ServerResponse;
import edu.itmo.piikt.common.util.DS;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Network implements Client {
	private static final int SIZE = 66666;
	private static final int TIME = 3000;
	private static final int CONNECTION_CLOSED = -1;
	private static final AppLogger logger = new AppLogger(Network.class);

	private SocketChannel socketChannel;
	private static final String HOST = System.getenv().getOrDefault("SERVER_HOST", "localhost");
	private final Integer PORT = 6668;
	private ClientData clientData;

	@Override
	public void connect() {
		connectWithRetry();
	}

	private void connectWithRetry() {
		while (!Thread.currentThread().isInterrupted()) {
			try (Context ignored = Context.newId()) {
				logger.info("Connecting to {}:{}", HOST, PORT);
				socketChannel = SocketChannel.open();
				socketChannel.configureBlocking(true);
				socketChannel.connect(new InetSocketAddress(HOST, PORT));
				clientData = new ClientData(SIZE);
				logger.info("Connected successfully");
				return;
			} catch (IOException e) {
				logger.warn("Retry interrupted");
			}
		}
	}

	@Override
	public ServerResponse send(ClientCommand clientResponse) throws Exception {
		try (Context ignored = Context.newId()) {
			if (socketChannel == null || !socketChannel.isConnected()) {
				connect();
			}
			logger.debug("Sending command: {}", clientResponse.getNameCommand());
			ByteBuffer writer = DS.serialize(clientResponse);
			socketChannel.write(writer);
			socketChannel.socket().setSoTimeout(TIME);
			ByteBuffer reader = clientData.getReader();
			reader.clear();
			int bytes = socketChannel.read(reader);
			if (bytes == CONNECTION_CLOSED) {
				logger.error("Connection closed by server");
				throw new IOException("Соединение закрыто");
			}
			reader.flip();
			ServerResponse serverResponse = (ServerResponse) DS.deserialize(reader);
			logger.debug("Response received: success={}", serverResponse.execution());
			return serverResponse;
		} catch (Exception e) {
			logger.error("Error sending command: {}", e);
			throw e;
		}
	}

	@Override
	public void close() throws IOException {
		try (Context ignored = Context.newId()) {
			logger.info("Closing connection");
			if (socketChannel != null) {
				socketChannel.close();
			}
			logger.info("Connection closed");
		} catch (IOException e) {
			logger.error("Error closing connection: {}", e);
			throw e;
		}
	}
}
