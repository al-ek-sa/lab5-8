package edu.itmo.piikt.server.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;

public class Websocket extends WebSocketServer {
	private final ObjectMapper objectMapper;
	private final ConcurrentHashMap<WebSocket, ClientSession> sessions;
	private final CollectionManager collectionManager;
	private final SecureRandom secureRandom = new SecureRandom();

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
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		sessions.remove(conn);
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		try {
			var root = objectMapper.readTree(message);
			String type = root.get("type").asText();

			if ("AUTH".equals(type)) {
				String login = root.get("login").asText();
				String password = root.get("password").asText();

				if (authenticateUser(login, password)) {
					String token = generateToken();
					ClientSession session = sessions.get(conn);
					session.setAuthenticated(true);
					session.setToken(token);
					session.setLogin(login);

					ObjectNode response = objectMapper.createObjectNode();
					response.put("type", "AUTH_SUCCESS");
					response.put("token", token);
					conn.send(response.toString());
				} else {
					ObjectNode response = objectMapper.createObjectNode();
					response.put("type", "AUTH_FAILED");
					conn.send(response.toString());
					conn.close();
				}
			} else if ("SYNC_REQUEST".equals(type)) {
				ClientSession session = sessions.get(conn);
				String token = root.has("token") ? root.get("token").asText() : null;

				if (session != null && session.isAuthenticated() && session.getToken().equals(token)) {
					sendFullSync(conn);
				} else {
					conn.close();
				}
			} else if ("DIFF_REQUEST".equals(type)) {
				ClientSession session = sessions.get(conn);
				String token = root.has("token") ? root.get("token").asText() : null;

				if (session != null && session.isAuthenticated() && session.getToken().equals(token)) {
					long fromVersion = root.has("version") ? root.get("version").asLong() : 0;
					sendDiff(conn, fromVersion);
				} else {
					conn.close();
				}
			}
		} catch (Exception e) {
			//
		}
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		if (conn != null) {
			sessions.remove(conn);
		}
	}

	@Override
	public void onStart() {
		//
	}

	private boolean authenticateUser(String login, String password) {
		if (login == null || password == null)
			return false;

		try {
			String hashedPassword = hashPassword(password);

			String sql = "SELECT id FROM \"user\" WHERE login = ? AND password = ?";
			try (var conn = BDConnect.INSTANCE.getConnection(); var stmt = conn.prepareStatement(sql)) {
				stmt.setString(1, login);
				stmt.setString(2, hashedPassword);
				var rs = stmt.executeQuery();
				return rs.next();
			}
		} catch (Exception e) {
			return false;
		}
	}

	private String hashPassword(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			byte[] hash = md.digest(password.getBytes());
			StringBuilder sb = new StringBuilder();
			for (byte b : hash) {
				sb.append(String.format("%02x", b));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			return password;
		}
	}

	private String generateToken() {
		byte[] bytes = new byte[32];
		secureRandom.nextBytes(bytes);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
	}

	private void sendFullSync(WebSocket conn) {
		try {
			long version = collectionManager.getCurrentVersion();
			ObjectNode message = objectMapper.createObjectNode();
			message.put("type", "FULL_SYNC");
			message.put("version", version);
			message.set("workers", collectionManager.getAllWorkersJson());

			conn.send(objectMapper.writeValueAsString(message));

			ClientSession session = sessions.get(conn);
			if (session != null) {
				session.setLastVersion(version);
			}
		} catch (Exception e) {
			//
		}
	}

	private void sendDiff(WebSocket conn, long fromVersion) {
		try {
			var updates = collectionManager.getUpdatesSince(fromVersion);
			for (String update : updates) {
				conn.send(update);
			}
		} catch (Exception e) {
			//
		}
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
				ClientSession session = sessions.get(conn);
				if (session != null && session.isAuthenticated()) {
					conn.send(json);
					session.setLastVersion(0);
				}
			}
		} catch (Exception e) {
			//
		}
	}

	@Setter
	@Getter
	private static class ClientSession {
		private long lastVersion;
		private String login;
		private String token;
		private boolean authenticated = false;
		private final String sessionId;

		public ClientSession(String sessionId, long lastVersion) {
			this.sessionId = sessionId;
			this.lastVersion = lastVersion;
		}
	}
}
