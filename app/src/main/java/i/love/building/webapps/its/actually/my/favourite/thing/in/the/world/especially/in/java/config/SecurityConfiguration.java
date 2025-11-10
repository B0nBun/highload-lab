package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.httpBasic(
                        sec -> {
                            sec.disable();
                        })
                .authorizeHttpRequests(
                        sec -> {
                            sec.anyRequest().permitAll();
                        })
                .csrf(
                        sec -> {
                            sec.disable();
                        })
                .build();
    }
}
