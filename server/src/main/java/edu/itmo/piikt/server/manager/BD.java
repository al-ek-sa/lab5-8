package edu.itmo.piikt.server.manager;

import lombok.Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Data
public class BD {
    private static final String URL = "jdbc:postgresql://localhost:6565/proga_lab";
    private final static String USER = "alexa";
    private static final String PASSWORD = "123";
    private Connection connection;
    public BD() throws SQLException {
        this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void close() throws SQLException {
        if(connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
