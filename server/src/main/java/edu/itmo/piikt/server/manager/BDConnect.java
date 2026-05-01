package edu.itmo.piikt.server.manager;

import lombok.Getter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.HexFormat;

/**
 * Database connection manager implemented as an enum singleton. Provides
 * database operations for user registration, authentication, and connection
 * management.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@Getter
public enum BDConnect {
	INSTANCE();
	private static final String URL = "jdbc:postgresql://localhost:6969/proga";
	private final static String USER = "alexa";
	private static final String PASSWORD = "123";
	private Connection connection;

	/**
	 * Establishes a connection to the database. Attempts to connect in a loop with
	 * a 100ms delay between attempts.
	 *
	 * @throws SQLException
	 *             if a database access error occurs
	 * @throws InterruptedException
	 *             if the thread is interrupted while sleeping
	 */
	public void connection() throws SQLException, InterruptedException {
		while (true) {
			try {
				connection = DriverManager.getConnection(URL, USER, PASSWORD);
				break;
			} catch (SQLException e) {
				Thread.sleep(100);
			}
		}
	}

	/**
	 * Closes the database connection if it is open.
	 *
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public void close() throws SQLException {
		if (connection != null && !connection.isClosed()) {
			connection.close();
		}
	}

	/**
	 * Checks whether the database connection is active and open
	 *
	 * @return true if connected, false otherwise
	 */
	public boolean isConnected() {
		try {
			return connection != null && !connection.isClosed();
		} catch (SQLException e) {
			return false;
		}
	}

	/**
	 * Hashes a password using the SHA-1 algorithm.
	 *
	 * @param password
	 *            the password to hash
	 * @return hexadecimal string representation of the hash
	 * @throws RuntimeException
	 *             if SHA-1 algorithm is not available
	 */
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
