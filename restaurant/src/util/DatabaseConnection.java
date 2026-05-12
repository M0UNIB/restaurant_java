package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static final String CONFIG_FILE = "conf.properties";

    private static Connection instance = null;

    private DatabaseConnection() {}

    public static Connection getInstance() throws SQLException {
        if (instance == null || instance.isClosed()) {
            try {
                Properties config = loadConfig();

                Class.forName("com.mysql.cj.jdbc.Driver");
                instance = DriverManager.getConnection(
                        config.getProperty("jdbc.url"),
                        config.getProperty("jdbc.user"),
                        config.getProperty("jdbc.password")
                );
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL JDBC Driver not found.", e);
            } catch (IOException e) {
                throw new SQLException("Unable to load database configuration.", e);
            }
        }
        return instance;
    }

    private static Properties loadConfig() throws IOException {
        Properties config = new Properties();

        try (InputStream input = openConfig()) {
            if (input == null) {
                throw new IOException(CONFIG_FILE + " not found.");
            }
            config.load(input);
        }

        return config;
    }

    private static InputStream openConfig() throws IOException {
        InputStream classpathConfig = DatabaseConnection.class
                .getClassLoader()
                .getResourceAsStream(CONFIG_FILE);

        if (classpathConfig != null) {
            return classpathConfig;
        }

        String[] paths = {
                CONFIG_FILE,
                "restaurant/" + CONFIG_FILE,
                "src/" + CONFIG_FILE,
                "restaurant/src/" + CONFIG_FILE,
                "../" + CONFIG_FILE,
                "../../" + CONFIG_FILE,
                "../../../" + CONFIG_FILE
        };

        for (String path : paths) {
            File config = new File(path);
            if (config.isFile()) {
                return new FileInputStream(config);
            }
        }

        throw new IOException(CONFIG_FILE + " not found.");
    }
}
