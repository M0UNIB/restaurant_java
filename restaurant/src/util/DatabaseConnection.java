package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL      = "jdbc:mysql://localhost:3306/gestion_restaurant";
    private static final String USER     = "root";
    private static final String PASSWORD = "pass";

    private static Connection instance = null;

    private DatabaseConnection() {}

    public static Connection getInstance() throws SQLException {
        if (instance == null || instance.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                instance = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL JDBC Driver not found.", e);
            }
        }
        return instance;
    }
}
