package edu.itmo.piikt.server.registration;

import edu.itmo.piikt.common.server_client.ServerResponse;
import edu.itmo.piikt.server.manager.BDConnect;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BD {
	public ServerResponse newUser(String email, String login, String password) {
		String sql = "insert into user(email, login, password) values (?, ?, ?)";
		try (PreparedStatement preparedStatement = BDConnect.INSTANCE.getConnection().prepareStatement(sql)) {
			preparedStatement.setString(1, login);
			preparedStatement.setString(2, BDConnect.INSTANCE.hashPassword(password));
			preparedStatement.setString(3, email);
			preparedStatement.executeUpdate();
			return ServerResponse.successfulCompletion("Вы вошли в аккаунт");
		} catch (SQLException e) {
			return ServerResponse.error("Данный логин уже занят");
		}
	}

	public ServerResponse login(String login, String password) {
		String sql = "select * from user where login = ? and password =?";
		try (PreparedStatement preparedStatement = BDConnect.INSTANCE.getConnection().prepareStatement(sql)) {
			preparedStatement.setString(1, login);
			preparedStatement.setString(2, BDConnect.INSTANCE.hashPassword(password));
			preparedStatement.executeUpdate();
			return ServerResponse.successfulCompletion("Вы вошли в аккаунт");
		} catch (SQLException e) {
			return ServerResponse.error("Не верный пароль или логин");
		}
	}

	public ServerResponse newPassword(String email, String login, String newPassword) {
		String sql = "update user set password = ? where login = ? and email = ?";
		try (PreparedStatement preparedStatement = BDConnect.INSTANCE.getConnection().prepareStatement(sql)) {
			preparedStatement.setString(1, BDConnect.INSTANCE.hashPassword(newPassword));
			preparedStatement.setString(2, login);
			preparedStatement.setString(3, email);
			preparedStatement.executeUpdate();
			return ServerResponse.successfulCompletion("пароль успешно обновлен");
		} catch (SQLException e) {
			return ServerResponse.error("ошибка обновления пароля");
		}
	}
}
