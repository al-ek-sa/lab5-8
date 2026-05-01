package edu.itmo.piikt.server.registration;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.sc.ServerResponse;
import edu.itmo.piikt.server.manager.BDConnect;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BD {
	private static final AppLogger logger = new AppLogger(BD.class);

	public ServerResponse newUser(String email, String login, String password) {
		try (Context ignored = Context.newId()) {
			String sql = "INSERT INTO \"user\"(email, login, password) VALUES (?, ?, ?)";

			logger.debug("Creating new user: login={}, email={}", login, email);

			try (PreparedStatement preparedStatement = BDConnect.INSTANCE.getConnection().prepareStatement(sql)) {
				preparedStatement.setString(1, email);
				preparedStatement.setString(2, login);
				preparedStatement.setString(3, BDConnect.INSTANCE.hashPassword(password));

				int rowsAffected = preparedStatement.executeUpdate();
				logger.info("User registered successfully: login={}, rowsAffected={}", login, rowsAffected);

				return ServerResponse.successfulCompletion("Registration successful");

			} catch (SQLException e) {
				if (e.getMessage().contains("duplicate key")) {
					logger.warn("Registration failed - duplicate login or email: login={}, email={}", login, email);
					return ServerResponse.error("Login or email already exists");
				}

				logger.error("Database error during registration: {}", e.getMessage(), e);
				return ServerResponse.error("Database error, please try again later");
			}
		} catch (Exception e) {
			logger.error("Unexpected error in newUser: {}", e.getMessage(), e);
			return ServerResponse.error("Internal server error");
		}
	}

	public ServerResponse login(String login, String password) {
		try (Context ignored = Context.newId()) {
			String sql = "SELECT id FROM \"user\" WHERE login = ? AND password = ?";

			logger.debug("Authenticating user: {}", login);

			try (PreparedStatement preparedStatement = BDConnect.INSTANCE.getConnection().prepareStatement(sql)) {
				preparedStatement.setString(1, login);
				preparedStatement.setString(2, BDConnect.INSTANCE.hashPassword(password));

				try (ResultSet rs = preparedStatement.executeQuery()) {
					if (rs.next()) {
						int userId = rs.getInt("id");
						logger.info("Login successful for user: {}, userId={}", login, userId);
						return ServerResponse.successfulCompletion("Login successful", userId);
					} else {
						logger.warn("Login failed for user: {} - invalid credentials", login);
						return ServerResponse.error("Invalid login or password");
					}
				}
			} catch (SQLException e) {
				logger.error("Database error during login: {}", e.getMessage(), e);
				return ServerResponse.error("Database error, please try again later");
			}
		} catch (Exception e) {
			logger.error("Unexpected error in login: {}", e.getMessage(), e);
			return ServerResponse.error("Internal server error");
		}
	}

	public ServerResponse newPassword(String email, String login, String newPassword) {
		try (Context ignored = Context.newId()) {
			String sql = "UPDATE \"user\" SET password = ? WHERE login = ? AND email = ?";

			logger.debug("Password reset requested for login: {}, email: {}", login, email);

			try (PreparedStatement preparedStatement = BDConnect.INSTANCE.getConnection().prepareStatement(sql)) {
				preparedStatement.setString(1, BDConnect.INSTANCE.hashPassword(newPassword));
				preparedStatement.setString(2, login);
				preparedStatement.setString(3, email);

				int rowsAffected = preparedStatement.executeUpdate();

				if (rowsAffected > 0) {
					logger.info("Password reset successful for login: {}", login);
					return ServerResponse.successfulCompletion("Password successfully updated");
				} else {
					logger.warn("Password reset failed - user not found: login={}, email={}", login, email);
					return ServerResponse.error("User with this login and email not found");
				}

			} catch (SQLException e) {
				logger.error("Database error during password reset: {}", e.getMessage(), e);
				return ServerResponse.error("Database error, please try again later");
			}
		} catch (Exception e) {
			logger.error("Unexpected error in newPassword: {}", e.getMessage(), e);
			return ServerResponse.error("Internal server error");
		}
	}
}
