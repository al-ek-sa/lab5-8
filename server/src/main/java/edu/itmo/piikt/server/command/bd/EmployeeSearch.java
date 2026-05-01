package edu.itmo.piikt.server.command.bd;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.server.manager.BDConnect;
import lombok.experimental.UtilityClass;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@UtilityClass
public class EmployeeSearch {
    private static final AppLogger logger = new AppLogger(EmployeeSearch.class);

    public static int idUser(ClientCommand clientCommand) {
        String sql = "SELECT id FROM \"user\" WHERE login = ? LIMIT 1";
        try (PreparedStatement preparedStatement = BDConnect.INSTANCE.getConnection().prepareStatement(sql)) {
            preparedStatement.setString(1, clientCommand.getUser());
            ResultSet res = preparedStatement.executeQuery();
            if (res.next()) {
                int userId = res.getInt("id");
                logger.debug("Found userId={} for login={}", userId, clientCommand.getUser());
                return userId;
            } else {
                logger.error("User not found: login={}", clientCommand.getUser());
                throw new RuntimeException("User not found: " + clientCommand.getUser());
            }
        } catch (SQLException e) {
            logger.error("SQL error while fetching userId: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
