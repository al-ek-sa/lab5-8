package edu.itmo.piikt.server.registration;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.models.Worker;
import edu.itmo.piikt.common.server_client.ClientCommand;
import edu.itmo.piikt.common.server_client.ServerResponse;
import edu.itmo.piikt.server.manager.BDConnect;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WorkerAdd {
	private static final AppLogger logger = new AppLogger(WorkerAdd.class);

	public ServerResponse newWorker(ClientCommand clientCommand, Worker worker) {
		try (Context ignored = Context.newId()) {
			logger.debug("Saving worker to database: id={}, name={}", worker.getUuid(), worker.getName());

			String sql = "INSERT INTO worker(worker_id, name, user_id) VALUES (?, ?, ?)";

			try (PreparedStatement preparedStatement = BDConnect.INSTANCE.getConnection().prepareStatement(sql)) {
				preparedStatement.setString(1, worker.getUuid());
				preparedStatement.setString(2, worker.getName());
				preparedStatement.setInt(3, idUser(clientCommand));

				int rowsAffected = preparedStatement.executeUpdate();
				logger.info("Worker saved successfully: id={}, rowsAffected={}", worker.getUuid(), rowsAffected);

				return ServerResponse.successfulCompletion("Worker added successfully");

			} catch (SQLException e) {
				if (e.getMessage().contains("duplicate key")) {
					logger.warn("Worker already exists: id={}", worker.getUuid());
					return ServerResponse.error("Worker with this ID already exists");
				}

				logger.error("Database error while saving worker: {}", e.getMessage(), e);
				return ServerResponse.error("Database error, please try again later");
			}
		} catch (Exception e) {
			logger.error("Unexpected error in newWorker: {}", e.getMessage(), e);
			return ServerResponse.error("Internal server error");
		}
	}

	private int idUser(ClientCommand clientCommand) {
		String sql = "SELECT id FROM \"user\" WHERE login = ? LIMIT 1";
		try (PreparedStatement preparedStatement = BDConnect.INSTANCE.getConnection().prepareStatement(sql)) {
			preparedStatement.setString(1, clientCommand.getUser());
			ResultSet res = preparedStatement.executeQuery();
			if (res.next()) {
				return res.getInt("id");
			} else {
				throw new RuntimeException("User not found: " + clientCommand.getUser());
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
