package edu.itmo.piikt.server.command.bd;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.logger.Context;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;
import edu.itmo.piikt.server.manager.BDConnect;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class WorkerDelete {
    private static final AppLogger logger = new AppLogger(WorkerDelete.class);

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