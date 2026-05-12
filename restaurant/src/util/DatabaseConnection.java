package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

public class DatabaseConnection {
    private static final String CONFIG_FILE = "conf.properties";
    private static final String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";
    private static final String DRIVER_JAR = "mysql-connector-j-9.5.0.jar";

    private static Connection instance = null;

    private DatabaseConnection() {}

    public static Connection getInstance() throws SQLException {
        if (instance == null || instance.isClosed()) {
            try {
                Properties config = loadConfig();

                config.load(new FileInputStream("conf.properties"));
                instance = DriverManager.getConnection(
                        config.getProperty("jdbc.url"),
                        config.getProperty("jdbc.user"),
                        config.getProperty("jdbc.password")
                );
            } catch (IOException e) {
                throw new SQLException("Unable to load database configuration.", e);
            }
        }
        return instance;
    }

    private static void loadDriver() throws SQLException {
        try {
            Class.forName(DRIVER_CLASS);
            return;
        } catch (ClassNotFoundException ignored) {
        }

        try {
            File driverJar = findDriverJar();
            URLClassLoader loader = new URLClassLoader(
                    new URL[]{driverJar.toURI().toURL()},
                    DatabaseConnection.class.getClassLoader()
            );
            Class<?> driverClass = Class.forName(DRIVER_CLASS, true, loader);
            Driver driver = (Driver) driverClass.getDeclaredConstructor().newInstance();
            DriverManager.registerDriver(new DriverShim(driver));
        } catch (Exception e) {
            throw new SQLException("MySQL JDBC Driver not found.", e);
        }
    }

    private static File findDriverJar() throws IOException {
        String[] paths = {
                "lib/" + DRIVER_JAR,
                "restaurant/lib/" + DRIVER_JAR,
                "../lib/" + DRIVER_JAR,
                "../../lib/" + DRIVER_JAR,
                "../../../lib/" + DRIVER_JAR
        };

        for (String path : paths) {
            File jar = new File(path);
            if (jar.isFile()) {
                return jar;
            }
        }

        throw new IOException(DRIVER_JAR + " not found.");
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

    private static class DriverShim implements Driver {
        private final Driver driver;

        DriverShim(Driver driver) {
            this.driver = driver;
        }

        public Connection connect(String url, Properties info) throws SQLException {
            return driver.connect(url, info);
        }

        public boolean acceptsURL(String url) throws SQLException {
            return driver.acceptsURL(url);
        }

        public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
            return driver.getPropertyInfo(url, info);
        }

        public int getMajorVersion() {
            return driver.getMajorVersion();
        }

        public int getMinorVersion() {
            return driver.getMinorVersion();
        }

        public boolean jdbcCompliant() {
            return driver.jdbcCompliant();
        }

        public Logger getParentLogger() {
            return Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        }
    }
}
