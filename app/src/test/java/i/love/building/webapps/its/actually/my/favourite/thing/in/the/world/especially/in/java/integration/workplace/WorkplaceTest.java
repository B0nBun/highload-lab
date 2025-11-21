package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.integration.workplace;

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

public class WorkplaceTest extends IntegrationTest {
    @Autowired private OfficeRepository offices;

    private Office testOffice;

    @BeforeAll
    void setup() {
        Office office = new Office("workplace-test-office", null);
        this.testOffice = this.offices.save(office);
    }

    @AfterAll
    void teardown() {
        this.offices.delete(this.testOffice);
    }

    @Test
    void testGetCreateUpdateDelete() throws Exception {
        var getAllReq = MockMvcRequestBuilders.get("/api/workplace/");
        this.mockMvc
                .perform(getAllReq)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));

        var createReq =
                MockMvcRequestBuilders.post("/api/workplace/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                String.format(
                                        """
    {"officeId": %d,"monitors": 2, "audioEquipment": "absent"}
                    """,
                                        this.testOffice.getId()));
        this.mockMvc.perform(createReq).andExpect(MockMvcResultMatchers.status().isOk());

        MvcResult result =
                this.mockMvc
                        .perform(getAllReq)
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(MockMvcResultMatchers.jsonPath("$[0].monitors", Matchers.is(2)))
                        .andExpect(
                                MockMvcResultMatchers.jsonPath(
                                        "$[0].audioEquipment", Matchers.is("absent")))
                        .andReturn();

        String response = result.getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("$[0].id");

        var updateReq =
                MockMvcRequestBuilders.put(String.format("/api/workplace/%d", id))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                {"monitors": 3, "audioEquipment": "headset"}
                    """);
        this.mockMvc.perform(updateReq).andExpect(MockMvcResultMatchers.status().isOk());

        var getOneReq = MockMvcRequestBuilders.get(String.format("/api/workplace/%d", id));
        this.mockMvc
                .perform(getOneReq)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.monitors", Matchers.is(3)))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.audioEquipment", Matchers.is("headset")));

        var deleteReq = MockMvcRequestBuilders.delete(String.format("/api/workplace/%d", id));
        this.mockMvc.perform(deleteReq).andExpect(MockMvcResultMatchers.status().isOk());

        this.mockMvc
                .perform(getAllReq)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }
}
