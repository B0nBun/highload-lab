package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import io.swagger.v3.core.jackson.ModelResolver;

@Configuration
public class OpenAPIConfiguration {
    @Bean
    public ModelResolver modelResolver(ObjectMapper mapper) {
        return new ModelResolver(mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE));
    }
}
