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
public class WorkerFlag {
	private static final AppLogger logger = new AppLogger(WorkerFlag.class);

	public static ServerResponse getWorkerIdsByUserId(ClientCommand clientCommand, String workerId) {
		String sql = "SELECT flag FROM worker WHERE user_id = ? and worker_id = ?";

		try (PreparedStatement ps = BDConnect.INSTANCE.getConnection().prepareStatement(sql)) {
			ps.setInt(1, EmployeeSearch.idUser(clientCommand));
			ps.setString(2, workerId);
			ResultSet res = ps.executeQuery();
			if (res.next()) {
				if (res.equals("+")) {
					return ServerResponse.successfulCompletion("zxcvbn");
				} else {
					return ServerResponse.error("sdfghjk");
				}
			} else {
				return ServerResponse.error("sdfghj");
			}
		} catch (SQLException e) {
			return ServerResponse.error("sdfghk");
		}
	}
}
