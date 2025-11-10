package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO: Check that deleting on all entities actually works
//       (it may not without cascading)
// TODO: Check that all unique constraints return correct HTTP statuses/responses
// TODO: UPDATE method endpoints
// TODO: Specify OpenAPI documentation for endpoints with errors
// TODO: Make all "list" endpoints pageable
// TODO: Configure default RequestMapping prefix "/api"

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
