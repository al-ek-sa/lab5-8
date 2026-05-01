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
 * Class for updating worker information in the database.
 * Performs updates of worker records with ownership verification.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
public class WorkerUpdate {
    private static final AppLogger logger = new AppLogger(WorkerUpdate.class);

    public ServerResponse updateWorker(ClientCommand clientCommand, Worker worker) {
        try (Context ignored = Context.newId()) {
            int userId = EmployeeSearch.idUser(clientCommand);
            String workerId = worker.getUuid();
            logger.debug("Updating worker in database: id={}, name={}, userId={}", workerId, worker.getName(), userId);
            String sql = "UPDATE worker SET name = ? WHERE worker_id = ? AND user_id = ?";
            try (PreparedStatement preparedStatement = BDConnect.INSTANCE.getConnection().prepareStatement(sql)) {
                preparedStatement.setString(1, worker.getName());
                preparedStatement.setString(2, workerId);
                preparedStatement.setInt(3, userId);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    logger.info("Worker updated successfully: id={}, rowsAffected={}", workerId, rowsAffected);
                    return ServerResponse.successfulCompletion("Worker updated successfully");
                } else {
                    logger.warn("Worker not found or not owned by user: id={}, userId={}", workerId, userId);
                    return ServerResponse.error("Worker not found or you don't have permission to update it");
                }
            } catch (SQLException e) {
                logger.error("Database error while updating worker: {}", e.getMessage(), e);
                return ServerResponse.error("Database error, please try again later");
            }
        } catch (Exception e) {
            logger.error("Unexpected error in updateWorker: {}", e.getMessage(), e);
            return ServerResponse.error("Internal server error");
        }
    }
}