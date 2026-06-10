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
//есть мертвый код
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
	private volatile boolean authenticated = false;
	private long lastVersion = 0;
	private String login = null;
	private String password = null;
	private String authToken = null;
	private Future<?> pingTask;
	private Future<?> reconnectTask;
	private final StringBuilder messageBuffer = new StringBuilder();

	public Websocket(String host, int port, Consumer<CollectionUpdate> onUpdate, Consumer<Boolean> onConnectionChange) {
		this.host = host;
		this.port = port;
		this.onUpdate = onUpdate;
		this.onConnectionChange = onConnectionChange;
		this.objectMapper = new ObjectMapper();
		this.objectMapper.findAndRegisterModules();
		this.httpClient = HttpClient.newHttpClient();
	}

	public void setCredentials(String login, String password) {
		this.login = login;
		this.password = password;
	}

	public void connect() {
		if (closing)
			return;

		if (login == null || password == null) {
			return;
		}
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
		authenticated = false;
		authToken = null;
		if (pingTask != null) {
			pingTask.cancel(true);
		}
		if (reconnectTask != null) {
			reconnectTask.cancel(true);
		}
		if (webSocket != null) {
			webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "Client closing");
		}
		scheduler.shutdown();
	}

	private void scheduleReconnect() {
		if (closing)
			return;
		reconnectTask = scheduler.schedule(() -> {
			if (!closing && !authenticated) {
				connect();
			}
		}, 3, TimeUnit.SECONDS);
	}

	private void startKeepAlive() {
		if (pingTask != null) {
			pingTask.cancel(false);
		}
		pingTask = scheduler.scheduleAtFixedRate(() -> {
			if (connected && authenticated && webSocket != null) {
				try {
					webSocket.sendPing(ByteBuffer.wrap(new byte[0]));
				} catch (Exception e) {
					//
				}
			}
		}, 30, 30, TimeUnit.SECONDS);
	}

	private void sendAuthentication() {
		if (login == null || password == null) {
			return;
		}
		if (webSocket == null) {
			return;
		}
		try {
			ObjectNode authMessage = objectMapper.createObjectNode();
			authMessage.put("type", "AUTH");
			authMessage.put("login", login);
			authMessage.put("password", password);
			String json = objectMapper.writeValueAsString(authMessage);
			webSocket.sendText(json, true);
		} catch (Exception e) {
			//
		}
	}

	private void requestFullSync() {
		if (!authenticated || authToken == null) {
			return;
		}
		try {
			ObjectNode request = objectMapper.createObjectNode();
			request.put("type", "SYNC_REQUEST");
			request.put("token", authToken);
			String json = objectMapper.writeValueAsString(request);
			webSocket.sendText(json, true);
		} catch (Exception e) {
			//
		}
	}
	private void requestDiff() {
		if (!authenticated || authToken == null) {
			return;
		}

		try {
			ObjectNode request = objectMapper.createObjectNode();
			request.put("type", "DIFF_REQUEST");
			request.put("version", lastVersion);
			request.put("token", authToken);
			String json = objectMapper.writeValueAsString(request);
			webSocket.sendText(json, true);
		} catch (Exception e) {
			//
		}
	}

	private class WebSocketListener implements WebSocket.Listener {

		@Override
		public void onOpen(WebSocket webSocket) {
			Websocket.this.webSocket = webSocket;
			connected = true;

			synchronized (messageBuffer) {
				messageBuffer.setLength(0);
			}
			sendAuthentication();
			webSocket.request(1);
		}

		@Override
		public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
			synchronized (messageBuffer) {
				messageBuffer.append(data);

				if (last) {
					String fullMessage = messageBuffer.toString();
					messageBuffer.setLength(0);

					try {
						JsonNode root = objectMapper.readTree(fullMessage);
						handleMessage(root);
					} catch (Exception e) {
						// todo
					}
				}
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
			authenticated = false;
			authToken = null;
			synchronized (messageBuffer) {
				messageBuffer.setLength(0);
			}
			if (onConnectionChange != null) {
				SwingUtilities.invokeLater(() -> onConnectionChange.accept(false));
			}
			scheduleReconnect();
		}

		@Override
		public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
			connected = false;
			authenticated = false;
			authToken = null;
			synchronized (messageBuffer) {
				messageBuffer.setLength(0);
			}
			if (onConnectionChange != null) {
				SwingUtilities.invokeLater(() -> onConnectionChange.accept(false));
			}
			if (!closing) {
				scheduleReconnect();
			}
			return null;
		}

		private void handleMessage(JsonNode root) {
			if (!root.has("type")) {
				return;
			}

			String type = root.get("type").asText();

			switch (type) {
				case "AUTH_SUCCESS" -> {
					authenticated = true;
					if (root.has("token")) {
						authToken = root.get("token").asText();
					}
					startKeepAlive();
					if (onConnectionChange != null) {
						SwingUtilities.invokeLater(() -> onConnectionChange.accept(true));
					}
					if (lastVersion > 0) {
						requestDiff();
					} else {
						requestFullSync();
					}
				}

				case "AUTH_FAILED" -> {
					authenticated = false;
					authToken = null;
					if (onConnectionChange != null) {
						SwingUtilities.invokeLater(() -> onConnectionChange.accept(false));
					}
					SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null,
							"WebSocket authentication failed!\nCheck your login and password.", "Authentication Error",
							JOptionPane.ERROR_MESSAGE));
					disconnect();
				}

				case "FULL_SYNC" -> {
					if (!authenticated) {
						return;
					}

					lastVersion = root.has("version") ? root.get("version").asLong() : 0;

					JsonNode workersNode = root.get("workers");

					if (workersNode == null) {
						return;
					}

					if (!workersNode.isArray()) {
						return;
					}

					if (onUpdate != null) {
						CollectionUpdate update = new CollectionUpdate.FullSync(workersNode);
						SwingUtilities.invokeLater(() -> onUpdate.accept(update));
					}
				}

				case "UPDATE" -> {
					if (!authenticated) {
						return;
					}
					long version = root.has("version") ? root.get("version").asLong() : 0;
					if (version > lastVersion) {
						lastVersion = version;
					}
					if (!root.has("operation")) {
						return;
					}
					String operation = root.get("operation").asText();
					JsonNode data = root.get("data");

					CollectionUpdate update = switch (operation) {
						case "ADD" -> {
							if (data != null) {
								yield new CollectionUpdate.Add(data);
							} else {
								yield null;
							}
						}
						case "UPDATE" -> {
							if (data != null) {
								yield new CollectionUpdate.Update(data);
							} else {
								yield null;
							}
						}
						case "REMOVE" -> {
							if (data != null && data.has("uuid")) {
								String uuid = data.get("uuid").asText();
								yield new CollectionUpdate.Remove(uuid);
							} else {
								yield null;
							}
						}
						case "CLEAR" -> {
							yield new CollectionUpdate.Clear();
						}
						default -> {
							yield null;
						}
					};

					if (update != null && onUpdate != null) {
						final CollectionUpdate finalUpdate = update;
						SwingUtilities.invokeLater(() -> onUpdate.accept(finalUpdate));
					}
				}
			}
		}
	}
}
