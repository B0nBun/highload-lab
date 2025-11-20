package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.ContainerContextInitializer;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.PostgresContainer;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.User;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

@Import(PostgresContainer.class)
@SpringBootTest()
@ActiveProfiles("test")
@AutoConfigureMockMvc()
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(
    initializers = {ContainerContextInitializer.class}
)
public class AppTest {
    @Test
    void foo() {
        System.out.println("foo test");
    }
    
    // @Autowired private MockMvc mockMvc;
    // @Autowired private UserRepository users;

    // private final String testUsername = "usertester";
    // private final String testPasswordHash = BCrypt.hashpw("plain-test-password", BCrypt.gensalt());

    // @BeforeAll
    // void setup() {
    //     var testUser = new User(1L, this.testUsername, this.testPasswordHash, User.Role.admin);
    //     this.users.save(testUser);
    // }

    // @Test
    // void getUserTest() throws Exception {
    //     var req =
    //             MockMvcRequestBuilders.get("/api/user")
    //                     .queryParam("page", "0")
    //                     .queryParam("size", "1");
    //     var expectResponse =
    //             String.format(
    //                     """
    //             [{"id": 1, "name": "%s", "password" "%s"}]
    //             """,
    //                     this.testUsername, this.testPasswordHash);
    //     this.mockMvc
    //             .perform(req)
    //             .andExpect(MockMvcResultMatchers.status().isOk())
    //             .andExpect(MockMvcResultMatchers.content().json(expectResponse));
    // }
}
