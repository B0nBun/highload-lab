package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.integration.meetingroom;

import com.jayway.jsonpath.JsonPath;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.IntegrationTest;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.Office;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.repository.OfficeRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class MeeetingRoomTest extends IntegrationTest {
    @Autowired private OfficeRepository offices;

    private Office testOffice;

    @BeforeAll
    void setup() {
        Office office = new Office("meeting-room-test-office", null);
        this.testOffice = this.offices.save(office);
    }

    @AfterAll
    void teardown() {
        this.offices.delete(this.testOffice);
    }

    @Test
    void testGetCreateUpdateDelete() throws Exception {
        var getAllReq = MockMvcRequestBuilders.get("/api/meeting-room/");
        this.mockMvc
                .perform(getAllReq)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));

        var createReq =
                MockMvcRequestBuilders.post("/api/meeting-room/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                String.format(
                                        """
    {"office_id": %d,"remote_available": false, "capacity": 3}
                    """,
                                        this.testOffice.getId()));
        this.mockMvc.perform(createReq).andExpect(MockMvcResultMatchers.status().isOk());

        MvcResult result =
                this.mockMvc
                        .perform(getAllReq)
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(
                                MockMvcResultMatchers.jsonPath(
                                        "$[0].remote_available", Matchers.is(false)))
                        .andExpect(MockMvcResultMatchers.jsonPath("$[0].capacity", Matchers.is(3)))
                        .andReturn();

        String response = result.getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("$[0].id");

        var updateReq =
                MockMvcRequestBuilders.put(String.format("/api/meeting-room/%d", id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                {"remote_available": true, "capacity": 4}
                    """);
        this.mockMvc.perform(updateReq).andExpect(MockMvcResultMatchers.status().isOk());

        var getOneReq = MockMvcRequestBuilders.get(String.format("/api/meeting-room/%d", id));
        this.mockMvc
                .perform(getOneReq)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.remote_available", Matchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.capacity", Matchers.is(4)));

        var deleteReq = MockMvcRequestBuilders.delete(String.format("/api/meeting-room/%d", id));
        this.mockMvc.perform(deleteReq).andExpect(MockMvcResultMatchers.status().isOk());

        this.mockMvc
                .perform(getAllReq)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }
}
