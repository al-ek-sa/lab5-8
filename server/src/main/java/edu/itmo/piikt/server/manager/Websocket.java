package edu.itmo.piikt.server.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Setter;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

public class Websocket extends WebSocketServer {
	private final ObjectMapper objectMapper;
	private final ConcurrentHashMap<WebSocket, ClientSession> sessions;
	private final CollectionManager collectionManager;

	public Websocket(int port, CollectionManager collectionManager) {
		super(new InetSocketAddress(port));
		this.collectionManager = collectionManager;
		this.objectMapper = new ObjectMapper();
		this.sessions = new ConcurrentHashMap<>();
		this.objectMapper.findAndRegisterModules();
		setConnectionLostTimeout(0);
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		String sessionId = conn.getRemoteSocketAddress().toString();
		sessions.put(conn, new ClientSession(sessionId, 0));
		sendFullSync(conn);
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		String sessionId = sessions.remove(conn) != null ? conn.getRemoteSocketAddress().toString() : "unknown";
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		try {
			var root = objectMapper.readTree(message);
			String type = root.get("type").asText();

			if ("SYNC_REQUEST".equals(type)) {
				sendFullSync(conn);
			} else if ("DIFF_REQUEST".equals(type)) {
				long fromVersion = root.has("version") ? root.get("version").asLong() : 0;
				sendDiff(conn, fromVersion);
			}
		} catch (Exception e) {
			// todo
		}
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		// todo
	}

	@Override
	public void onStart() {
		// todo
	}

	public void broadcastUpdate(String operation, ObjectNode data) {
		if (sessions.isEmpty())
			return;

		try {
			ObjectNode message = objectMapper.createObjectNode();
			message.put("type", "UPDATE");
			message.put("operation", operation);
			message.set("data", data);

			String json = objectMapper.writeValueAsString(message);

			for (WebSocket conn : sessions.keySet()) {
				conn.send(json);
				ClientSession session = sessions.get(conn);
				if (session != null) {
					session.setLastVersion(0);
				}
			}
		} catch (Exception e) {
			// todo
		}
	}

	private void sendFullSync(WebSocket conn) {
		try {
			long version = collectionManager.getCurrentVersion();
			ObjectNode message = objectMapper.createObjectNode();
			message.put("type", "FULL_SYNC");
			message.put("version", version);
			message.set("workers", collectionManager.getAllWorkersJson());

			String json = objectMapper.writeValueAsString(message);
			conn.send(json);

			ClientSession session = sessions.get(conn);
			if (session != null) {
				session.setLastVersion(version);
			}
		} catch (Exception e) {
			// todo
		}
	}

	private void sendDiff(WebSocket conn, long fromVersion) {
		try {
			var updates = collectionManager.getUpdatesSince(fromVersion);

			for (String update : updates) {
				conn.send(update);
			}

			long lastVersion = collectionManager.getCurrentVersion();
			ClientSession session = sessions.get(conn);
			if (session != null) {
				session.setLastVersion(lastVersion);
			}
		} catch (Exception e) {
			// todo
		}
	}

	@Setter
	private static class ClientSession {
		private long lastVersion;

		public ClientSession(String sessionId, long lastVersion) {
			this.lastVersion = lastVersion;
		}

	}
}
