package edu.itmo.piikt.server.command.bd;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.models.Worker;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.server.manager.BDConnect;
import lombok.experimental.UtilityClass;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for filtering workers by user ownership.
 * Performs permission checks for each worker in the provided list.
 *
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@UtilityClass
public class SearchWorkerList {
	private static final AppLogger logger = new AppLogger(SearchWorkerList.class);

	/**
	 * Filters a list of workers, returning only those that belong to the specified user.
	 * For each worker, checks database to verify ownership.
	 *
	 * @param clientCommand client command containing user authentication information
	 * @param listWorker    list of workers to filter
	 * @return filtered list of workers owned by the user
	 */
	public List<Worker> searchWorkerList(ClientCommand clientCommand, List<Worker> listWorker) {
		String sql = "SELECT worker_id FROM worker WHERE user_id = ? and worker_id = ?";
		List<Worker> list = new ArrayList<>();
		for (Worker worker : listWorker) {
			try (PreparedStatement ps = BDConnect.INSTANCE.getConnection().prepareStatement(sql)) {
				ps.setInt(1, EmployeeSearch.idUser(clientCommand));
				ps.setString(2, worker.getUuid());
				ResultSet res = ps.executeQuery();
				if (res.next()) {
					list.add(worker);
				}
			} catch (SQLException e) {
				logger.error("Error checking worker ownership for {}: {}", worker.getUuid(), e.getMessage(), e);
				throw new RuntimeException(e);
			}
		}
		logger.debug("Filtered workers: {} owned out of {}", list.size(), listWorker.size());
		return list;
	}
}
