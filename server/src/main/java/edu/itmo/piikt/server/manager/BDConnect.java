package edu.itmo.piikt.server.manager;

import lombok.Getter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.HexFormat;

import static java.lang.Thread.sleep;

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
	private static final String URL = System.getenv("DB_URL");
	private static final String USER = System.getenv("DB_USER");
	private static final String PASSWORD = System.getenv("DB_PASSWORD");
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
		if (URL == null || USER == null || PASSWORD == null) {
			throw new IllegalStateException("Database credentials not set in environment variables");
		}
		while (true) {
			try {
				connection = DriverManager.getConnection(URL, USER, PASSWORD);
				break;
			} catch (SQLException e) {
				sleep(100);
			}
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
}
