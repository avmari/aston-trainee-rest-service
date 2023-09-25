package util;

import db.ConnectionManager;
import org.testcontainers.containers.PostgreSQLContainer;

public abstract class AbstractTest {
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13.4");

    static {
        postgres.withInitScript("sql/test.sql");
        postgres.start();
        ConnectionManager.setHikariConfigProperties(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword());
    }
}

