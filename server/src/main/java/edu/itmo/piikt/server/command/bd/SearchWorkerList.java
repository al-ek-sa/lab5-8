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

@UtilityClass
public class SearchWorkerList {
	private static final AppLogger logger = new AppLogger(SearchWorkerList.class);

	/**
	 * Retrieves the user ID associated with the specified client command. Executes
	 * a SQL query to find the user by login from the command.
	 *
	 * @return user identifier (primary key) from the database
	 * @throws RuntimeException
	 *             if user is not found or a database error occurs
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
				throw new RuntimeException(e);
			}
		}
		return list;
	}
}
