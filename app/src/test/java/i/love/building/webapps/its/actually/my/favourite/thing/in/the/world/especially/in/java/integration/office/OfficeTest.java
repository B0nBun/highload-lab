package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.integration.office;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.jayway.jsonpath.JsonPath;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.IntegrationTest;

public class OfficeTest extends IntegrationTest {
    @Test
    void canGetCreateAndDelete() throws Exception {
        var getAllReq = MockMvcRequestBuilders.get("/api/office/");
        this.mockMvc.perform(getAllReq).andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));

        var createReq = MockMvcRequestBuilders.post("/api/office/")
            .content("""
    {"name": "test-office-name", "map": null}
                    """)
            .contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(createReq).andExpect(MockMvcResultMatchers.status().isOk());

        MvcResult result = this.mockMvc.perform(getAllReq).andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is("test-office-name")))
            .andReturn();

        String response = result.getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("$[0].id");
        
        var getOneReq = MockMvcRequestBuilders.get(String.format("/api/office/%d", id));
        this.mockMvc.perform(getOneReq).andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("test-office-name")));

        var deleteReq = MockMvcRequestBuilders.delete(String.format("/api/office/%d", id));
        this.mockMvc.perform(deleteReq).andExpect(MockMvcResultMatchers.status().isOk());

        this.mockMvc.perform(getAllReq).andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }
}
