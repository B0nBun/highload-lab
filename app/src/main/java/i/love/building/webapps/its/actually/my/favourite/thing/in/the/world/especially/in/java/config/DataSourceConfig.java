package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DataSourceConfig {
    @Value("#{environment.POSTGRES_HOST}")
    String postgresHost;

    @Value("#{environment.POSTGRES_DB}")
    String postgresDb;

    @Primary
    @Bean
    public DataSource getDataSource() {
        String url = String.format("jdbc:postgresql://%s:5432/%s?stringtype=unspecified", postgresHost, postgresDb);
        DataSourceBuilder<?> b = DataSourceBuilder.create();
        return b
            .driverClassName("org.postgresql.Driver")
            .url(url)
            .username("booking")
            .password("password")
            .build();
    }
}
