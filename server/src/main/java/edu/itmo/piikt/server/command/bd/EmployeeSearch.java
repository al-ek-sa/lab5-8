package edu.itmo.piikt.server.command.bd;

import edu.itmo.piikt.common.logger.AppLogger;
import edu.itmo.piikt.common.sc.ClientCommand;
import edu.itmo.piikt.server.manager.BDConnect;
import lombok.experimental.UtilityClass;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Utility class for retrieving user identifier from database by login.
 * Provides a single method to fetch user ID from the "user" table.
 * @author Lishyk Aliaksandra
 * @version 1.0
 */
@UtilityClass
public class EmployeeSearch {
    private static final AppLogger logger = new AppLogger(EmployeeSearch.class);

    /**
     * Retrieves the user ID associated with the specified client command.
     * Executes a SQL query to find the user by login from the command.
     *
     * @param clientCommand client command containing the user login ({@link ClientCommand#getUser()})
     * @return user identifier (primary key) from the database
     * @throws RuntimeException if user is not found or a database error occurs
     */
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
