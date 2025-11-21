package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common;

import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest()
@ActiveProfiles("test")
@AutoConfigureMockMvc()
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(initializers = {ContainerContextInitializer.class})
@Testcontainers(disabledWithoutDocker = true)
public class IntegrationTest {
    @Autowired protected MockMvc mockMvc;
}
