package edu.itmo.piikt.server.command.bd;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.models.Worker;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;
import edu.itmo.piikt.server.manager.BDConnect;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Class for adding new workers to the database. Handles insertion of worker
 * records into the worker table with user association.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
public class WorkerAdd {
	private static final AppLogger logger = new AppLogger(WorkerAdd.class);

	/**
	 * Saves a new worker to the database. Inserts a record into the worker table
	 * with worker ID, name, and associated user ID.
	 *
	 * @param clientCommand
	 *            client command containing user authentication information
	 * @param worker
	 *            worker object to be saved containing UUID, name and other fields
	 * @return ServerResponse indicating success or failure of the operation
	 */
	public ServerResponse newWorker(ClientCommand clientCommand, Worker worker) {
		try (Context ignored = Context.newId()) {
			logger.debug("Saving worker to database: id={}, name={}", worker.getUuid(), worker.getName());

			String sql = "INSERT INTO worker(worker_id, name, user_id, flag) VALUES (?, ?, ?, ?)";

			try (PreparedStatement preparedStatement = BDConnect.INSTANCE.getConnection().prepareStatement(sql)) {
				preparedStatement.setString(1, worker.getUuid());
				preparedStatement.setString(2, worker.getName());
				preparedStatement.setInt(3, EmployeeSearch.idUser(clientCommand));
				preparedStatement.setString(4, clientCommand.getArgumentCommand());

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
}
