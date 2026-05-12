package util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static Connection connection;

    public static Connection getInstance() throws SQLException {

        try {
            if (connection == null || connection.isClosed()) {

                Properties props = new Properties();

                InputStream input = DatabaseConnection.class
                        .getClassLoader()
                        .getResourceAsStream("conf.properties");

                if (input == null) {
                    throw new SQLException("conf.properties not found");
                }

                props.load(input);

                Class.forName("com.mysql.cj.jdbc.Driver");

                connection = DriverManager.getConnection(
                        props.getProperty("jdbc.url"),
                        props.getProperty("jdbc.user"),
                        props.getProperty("jdbc.password")
                );
            }

        } catch (Exception e) {
            throw new SQLException("Database connection failed", e);
        }

        return connection;
    }
}