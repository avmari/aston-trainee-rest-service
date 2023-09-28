package db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import util.PropertiesUtil;

import java.sql.Connection;
import java.sql.SQLException;

public final class ConnectionManager {

    private static final String URL_KEY = "db.url";
    private static final String USERNAME_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";

    private static HikariDataSource dataSource;
    private static final HikariConfig config = new HikariConfig();

    static {
        config.setJdbcUrl(PropertiesUtil.getProperty(URL_KEY));
        config.setUsername(PropertiesUtil.getProperty(USERNAME_KEY));
        config.setPassword(PropertiesUtil.getProperty(PASSWORD_KEY));
        config.setDriverClassName(org.postgresql.Driver.class.getName());
    }

    private ConnectionManager() {}

    public static void setHikariConfigProperties(String jdbcUrl, String username, String password) {
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(org.postgresql.Driver.class.getName());
    }

    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            dataSource = new HikariDataSource(config);
        }
        return dataSource.getConnection();
    }
}
