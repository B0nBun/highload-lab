package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

public class ContainerContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        PostgresContainer.start(applicationContext);
    }
}
