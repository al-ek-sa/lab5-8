package edu.itmo.piikt.server.manager;

import lombok.Getter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.HexFormat;

@Getter
public enum BDConnect {
	INSTANCE();
	private static final String URL = "jdbc:postgresql://localhost:6969/proga";
	private final static String USER = "alexa";
	private static final String PASSWORD = "123";
	private Connection connection;

	public void connection() throws SQLException, InterruptedException {
		while (true) {
			try{
				connection = DriverManager.getConnection(URL, USER, PASSWORD);
				break;
			} catch (SQLException e) {
				Thread.sleep(100);
			}
		}
	}

	public void close() throws SQLException {
		if (connection != null && !connection.isClosed()) {
			connection.close();
		}
	}

	public String hashPassword(String password) {
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
			preparedStatement.setString(1, login);
			preparedStatement.setString(2, hashPassword(password));
			preparedStatement.setString(3, email);
			preparedStatement.executeUpdate();
			return true;
		} catch (SQLException e) {
			return false;
		}
    }

	public boolean login(String login, String password) {
		String sql = "select id from user where login = ? and password = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
			preparedStatement.setString(1, login);
			preparedStatement.setString(2, hashPassword(password));
			ResultSet res = preparedStatement.executeQuery();
			if (res.next()) {
				return true;
			}
		} catch (SQLException e) {
            return false;
        }
		return false;
    }
}
