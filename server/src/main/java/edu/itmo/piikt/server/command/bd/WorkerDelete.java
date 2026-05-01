package edu.itmo.piikt.server.command.bd;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;
import edu.itmo.piikt.server.manager.BDConnect;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Class for deleting workers from the database.
 * Performs deletion of worker records with ownership verification.
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
public class WorkerDelete {
    private static final AppLogger logger = new AppLogger(WorkerDelete.class);

    /**
     * Deletes a worker from the database by its ID.
     * The deletion is only performed if the worker belongs to the user specified in the command.
     *
     * @param clientCommand client command containing user authentication information
     * @param workerId      unique identifier of the worker to delete
     * @return ServerResponse indicating success or failure of the operation
     */
    public ServerResponse deleteWorker(ClientCommand clientCommand, String workerId) {
        try (Context ignored = Context.newId()) {
            int userId = EmployeeSearch.idUser(clientCommand);
            logger.debug("Deleting worker: id={}, userId={}", workerId, userId);
            String sql = "DELETE FROM worker WHERE user_id = ? AND worker_id = ?";

            try (PreparedStatement preparedStatement = BDConnect.INSTANCE.getConnection().prepareStatement(sql)) {
                preparedStatement.setInt(1, userId);
                preparedStatement.setString(2, workerId);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    logger.info("Worker deleted successfully: id={}, rowsAffected={}", workerId, rowsAffected);
                    return ServerResponse.successfulCompletion("Worker deleted successfully");
                } else {
                    logger.warn("Worker not found or not owned by user: id={}, userId={}", workerId, userId);
                    return ServerResponse.error("Worker not found or you don't have permission to delete it");
                }

            } catch (SQLException e) {
                logger.error("Database error while deleting worker: {}", e.getMessage(), e);
                return ServerResponse.error("Database error, please try again later");
            }
        } catch (Exception e) {
            logger.error("Unexpected error in deleteWorker: {}", e.getMessage(), e);
            return ServerResponse.error("Internal server error");
        }
    }
}