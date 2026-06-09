package edu.itmo.piikt.client.webSocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.swing.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public class Websocket {
	private final HttpClient httpClient;
	private final ObjectMapper objectMapper;
	private final String host;
	private final int port;
	private final Consumer<CollectionUpdate> onUpdate;
	private final Consumer<Boolean> onConnectionChange;
	private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

	private WebSocket webSocket;
	private volatile boolean connected = false;
	private volatile boolean closing = false;
	private long lastVersion = 0;
	private Future<?> pingTask;
	private Future<?> reconnectTask;

	public Websocket(String host, int port, Consumer<CollectionUpdate> onUpdate, Consumer<Boolean> onConnectionChange) {
		this.host = host;
		this.port = port;
		this.onUpdate = onUpdate;
		this.onConnectionChange = onConnectionChange;
		this.objectMapper = new ObjectMapper();
		this.objectMapper.findAndRegisterModules();
		this.httpClient = HttpClient.newHttpClient();
	}

	public void connect() {
		if (closing)
			return;

		if (reconnectTask != null) {
			reconnectTask.cancel(false);
		}

		try {
			String serverUrl = String.format("ws://%s:%d", host, port);
			httpClient.newWebSocketBuilder().buildAsync(URI.create(serverUrl), new WebSocketListener())
					.thenAccept(ws -> {
						webSocket = ws;
					}).exceptionally(e -> {
						scheduleReconnect();
						return null;
					});
		} catch (Exception e) {
			scheduleReconnect();
		}
	}

	public void disconnect() {
		closing = true;
		scheduler.shutdown();
		if (pingTask != null) {
			pingTask.cancel(true);
		}
		if (reconnectTask != null) {
			reconnectTask.cancel(true);
		}
		if (webSocket != null) {
			webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "Client closing");
		}
	}

	private void scheduleReconnect() {
		if (closing)
			return;

		reconnectTask = scheduler.schedule(() -> {
			if (!closing) {
				connect();
			}
		}, 3, TimeUnit.SECONDS);
	}

	private void startKeepAlive() {
		if (pingTask != null) {
			pingTask.cancel(false);
		}
		pingTask = scheduler.scheduleAtFixedRate(() -> {
			if (connected && webSocket != null) {
				try {
					webSocket.sendPing(ByteBuffer.wrap(new byte[0]));
				} catch (Exception e) {
					//
				}
			}
		}, 30, 30, TimeUnit.SECONDS);
	}

	private void requestDiff() {
		try {
			ObjectNode request = objectMapper.createObjectNode();
			request.put("type", "DIFF_REQUEST");
			request.put("version", lastVersion);
			String json = objectMapper.writeValueAsString(request);
			webSocket.sendText(json, true);
		} catch (Exception e) {
			// todo
		}
	}

	private void requestFullSync() {
		try {
			ObjectNode request = objectMapper.createObjectNode();
			request.put("type", "SYNC_REQUEST");
			String json = objectMapper.writeValueAsString(request);
			webSocket.sendText(json, true);
		} catch (Exception e) {
			// todo
		}
	}

	private class WebSocketListener implements WebSocket.Listener {

		@Override
		public void onOpen(WebSocket webSocket) {
			connected = true;
			startKeepAlive();
			SwingUtilities.invokeLater(() -> onConnectionChange.accept(true));
			if (lastVersion > 0) {
				requestDiff();
			} else {
				requestFullSync();
			}
			webSocket.request(1);
		}

		@Override
		public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
			String message = data.toString();

			try {
				JsonNode root = objectMapper.readTree(message);
				handleMessage(root);
			} catch (Exception e) {
				// todo
			}

			webSocket.request(1);
			return null;
		}

		@Override
		public CompletionStage<?> onPing(WebSocket webSocket, ByteBuffer message) {
			webSocket.sendPong(ByteBuffer.wrap(new byte[0]));
			return null;
		}

		@Override
		public void onError(WebSocket webSocket, Throwable error) {
			connected = false;
			SwingUtilities.invokeLater(() -> onConnectionChange.accept(false));
			scheduleReconnect();
		}

		@Override
		public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
			connected = false;
			SwingUtilities.invokeLater(() -> onConnectionChange.accept(false));
			if (!closing) {
				scheduleReconnect();
			}
			return null;
		}

		private void handleMessage(JsonNode root) {
			String type = root.get("type").asText();

			switch (type) {
				case "FULL_SYNC" -> {
					lastVersion = root.has("version") ? root.get("version").asLong() : 0;
					CollectionUpdate update = new CollectionUpdate.FullSync(root.get("workers"));
					SwingUtilities.invokeLater(() -> onUpdate.accept(update));
				}
				case "UPDATE" -> {
					long version = root.has("version") ? root.get("version").asLong() : 0;
					if (version > lastVersion) {
						lastVersion = version;
					}
					String operation = root.get("operation").asText();
					JsonNode data = root.get("data");

					CollectionUpdate update = switch (operation) {
						case "ADD" -> new CollectionUpdate.Add(data);
						case "UPDATE" -> new CollectionUpdate.Update(data);
						case "REMOVE" -> new CollectionUpdate.Remove(data.get("uuid").asText());
						case "CLEAR" -> new CollectionUpdate.Clear();
						default -> null;
					};

					if (update != null) {
						SwingUtilities.invokeLater(() -> onUpdate.accept(update));
					}
				}
				default -> {
				}
			}
		}
	}
}
