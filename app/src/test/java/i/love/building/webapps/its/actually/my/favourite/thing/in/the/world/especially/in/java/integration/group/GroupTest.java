package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.integration.group;

import com.jayway.jsonpath.JsonPath;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.IntegrationTest;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.Office;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.User;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.repository.OfficeRepository;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.repository.UserRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@TestMethodOrder(OrderAnnotation.class)
public class GroupTest extends IntegrationTest {
    @Autowired private UserRepository users;
    @Autowired private OfficeRepository offices;

    private User testUser;
    private Office testOffice;

    @BeforeAll
    void setup() {
        User user =
                new User("group-test-user", BCrypt.hashpw("", BCrypt.gensalt()), User.Role.admin);
        this.testUser = this.users.save(user);
        Office office = new Office("group-test-office", null);
        this.testOffice = this.offices.save(office);
    }

    @AfterAll
    void teardown() {
        this.users.delete(this.testUser);
        this.offices.delete(this.testOffice);
    }

    @Test
    @Order(1)
    void canCreate() throws Exception {
        var getAllReq = MockMvcRequestBuilders.get("/api/group/");
        this.mockMvc
                .perform(getAllReq)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));

        var createReq =
                MockMvcRequestBuilders.post("/api/group/")
                        .content(
                                """
    {"name": "test-group-name"}
                    """)
                        .contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(createReq).andExpect(MockMvcResultMatchers.status().isOk());

        this.mockMvc
                .perform(getAllReq)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                "$[0].name", Matchers.is("test-group-name")));
    }

    @Test
    @Order(2)
    void canAddAndDeleteUsers() throws Exception {
        Integer id = this.getTestGroupId();

        var getReq = MockMvcRequestBuilders.get(String.format("/api/group/%d", id));
        this.mockMvc
                .perform(getReq)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userIds", Matchers.hasSize(0)));

        var addUserReq =
                MockMvcRequestBuilders.post(String.format("/api/group/%d/users", id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                String.format(
                                        """
                    {"userId": %d}
                    """,
                                        this.testUser.getId()));
        this.mockMvc.perform(addUserReq).andExpect(MockMvcResultMatchers.status().isOk());

        this.mockMvc
                .perform(getReq)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                "$.userIds[0]", Matchers.is(testUser.getId().intValue())));

        var removeUserReq =
                MockMvcRequestBuilders.delete(
                        String.format("/api/group/%d/users/%d", id, this.testUser.getId()));
        this.mockMvc.perform(removeUserReq).andExpect(MockMvcResultMatchers.status().isOk());

        this.mockMvc
                .perform(getReq)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userIds", Matchers.hasSize(0)));
    }

    @Test
    @Order(3)
    void canAddAndDeleteOffices() throws Exception {
        Integer id = this.getTestGroupId();

        var getReq = MockMvcRequestBuilders.get(String.format("/api/group/%d", id));
        this.mockMvc
                .perform(getReq)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.officeIds", Matchers.hasSize(0)));

        var addReq =
                MockMvcRequestBuilders.post(String.format("/api/group/%d/offices", id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                String.format(
                                        """
                    {"officeId": %d}
                    """,
                                        this.testOffice.getId()));
        this.mockMvc.perform(addReq).andExpect(MockMvcResultMatchers.status().isOk());

        this.mockMvc
                .perform(getReq)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                "$.officeIds[0]", Matchers.is(this.testOffice.getId().intValue())));

        var removeReq =
                MockMvcRequestBuilders.delete(
                        String.format("/api/group/%d/offices/%d", id, this.testOffice.getId()));
        this.mockMvc.perform(removeReq).andExpect(MockMvcResultMatchers.status().isOk());

        this.mockMvc
                .perform(getReq)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.officeIds", Matchers.hasSize(0)));
    }

    @Test
    @Order(4)
    void canDelete() throws Exception {
        Integer id = this.getTestGroupId();

        var getOneReq = MockMvcRequestBuilders.get(String.format("/api/group/%d", id));
        this.mockMvc
                .perform(getOneReq)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.name", Matchers.is("test-group-name")));

        var deleteReq = MockMvcRequestBuilders.delete(String.format("/api/group/%d", id));
        this.mockMvc.perform(deleteReq).andExpect(MockMvcResultMatchers.status().isOk());

        var getAllReq = MockMvcRequestBuilders.get("/api/group/");
        this.mockMvc
                .perform(getAllReq)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }

    private Integer getTestGroupId() throws Exception {
        var getAllReq = MockMvcRequestBuilders.get("/api/group/");
        MvcResult result =
                this.mockMvc
                        .perform(getAllReq)
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(
                                MockMvcResultMatchers.jsonPath(
                                        "$[0].name", Matchers.is("test-group-name")))
                        .andReturn();

        String response = result.getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("$[0].id");
        return id;
    }
}
