package edu.itmo.piikt.server.manager;

import lombok.Data;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HexFormat;

@Data
public class BD {
	private static final String URL = "jdbc:postgresql://localhost:6565/proga_lab";
	private final static String USER = "alexa";
	private static final String PASSWORD = "123";
	private Connection connection;
	public BD() throws SQLException {
		this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
	}

	public void close() throws SQLException {
		if (connection != null && !connection.isClosed()) {
			connection.close();
		}
	}

	private String hasPassword(String password) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
			byte[] hash = messageDigest.digest(password.getBytes());
			return HexFormat.of().formatHex(hash);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean register(String login, String password, String email) {
		String sql = "insert into user(login, password, email) values (?, ?, ?)";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

		}
	}
}
