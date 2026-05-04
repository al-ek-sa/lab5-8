package edu.itmo.piikt.server.command.bd;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;
import edu.itmo.piikt.server.manager.BDConnect;
import lombok.experimental.UtilityClass;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Utility class for checking user permissions on worker records. Provides
 * methods to verify if a user has rights to access specific workers.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@UtilityClass
public class SearchWorker {
	private static final AppLogger logger = new AppLogger(SearchWorker.class);

	/**
	 * Checks if the user has permissions to modify the specified worker. Executes a
	 * SQL query to verify that both user_id and worker_id exist.
	 *
	 * @param clientCommand
	 *            client command containing user authentication information
	 * @param workerId
	 *            unique identifier of the worker to check
	 * @return ServerResponse indicating whether user has permissions
	 */
	public static ServerResponse getWorkerIdsByUserId(ClientCommand clientCommand, String workerId) {
		String sql = "SELECT worker_id FROM worker WHERE user_id = ? and worker_id = ?";

		try (PreparedStatement ps = BDConnect.INSTANCE.getConnection().prepareStatement(sql)) {
			ps.setInt(1, EmployeeSearch.idUser(clientCommand));
			ps.setString(2, workerId);
			ResultSet res = ps.executeQuery();
			if (res.next()) {
				logger.debug("User has permission for worker: {}", workerId);
				return ServerResponse.successfulCompletion("User has permission to perform actions on this record");
			} else {
				logger.warn("User does not have permission for worker: {}", workerId);
				return ServerResponse.error("You do not have permission to work with this record");
			}
		} catch (SQLException e) {
			logger.error("Error checking user permissions: {}", e.getMessage(), e);
			return ServerResponse.error("Error checking permissions");
		}
	}
}
