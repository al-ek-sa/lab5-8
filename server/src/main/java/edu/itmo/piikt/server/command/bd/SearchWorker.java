package edu.itmo.piikt.server.command.bd;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.common.sc.ServerResponse;
import edu.itmo.piikt.server.manager.BDConnect;
import lombok.experimental.UtilityClass;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@UtilityClass
public class SearchWorker {
	private static final AppLogger logger = new AppLogger(SearchWorker.class);

	/**
	 * Retrieves the user ID associated with the specified client command. Executes
	 * a SQL query to find the user by login from the command.
	 *
	 * @return user identifier (primary key) from the database
	 * @throws RuntimeException
	 *             if user is not found or a database error occurs
	 */
	public static ServerResponse getWorkerIdsByUserId(ClientCommand clientCommand, String workerId) {
		String sql = "SELECT worker_id FROM worker WHERE user_id = ? and worker_id = ?";

		try (PreparedStatement ps = BDConnect.INSTANCE.getConnection().prepareStatement(sql)) {
			ps.setInt(1, EmployeeSearch.idUser(clientCommand));
			ps.setString(2, workerId);
			ResultSet res = ps.executeQuery();
			if (res.next()) {
				return ServerResponse
						.successfulCompletion("У пользователя есть права для выполнения действий с этой записью");
			} else {
				return ServerResponse.error("У Вас нет прав для работы с этой записью");
			}
		} catch (SQLException e) {
			return ServerResponse.error("Ошибка выполнения");
		}
	}
}
