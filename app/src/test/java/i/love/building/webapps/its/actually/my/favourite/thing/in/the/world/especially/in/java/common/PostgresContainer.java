package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresContainer {
    static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres")
            .withDatabaseName("booking_db")
            .withUsername("booking")
            .withPassword("password");

    static void start(ConfigurableApplicationContext ctx) {
        postgresContainer.start();
        TestPropertyValues.of(
            "spring.datasource.url=" + postgresContainer.getJdbcUrl(),
            "spring.datasource.username=" + postgresContainer.getUsername(),
            "spring.datasource.password=" + postgresContainer.getPassword()
        ).applyTo(ctx);
    }
}
