package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO: Specify OpenAPI documentation for endpoints with errors

@RestController
@SpringBootApplication
public class App {
    @Hidden
    @GetMapping(value = "/")
    String home() {
        return "<a href=\"/docs/swagger\">Swagger UI</a><br><a href=\"/docs/openapi\">OpenAPI</a>";
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
