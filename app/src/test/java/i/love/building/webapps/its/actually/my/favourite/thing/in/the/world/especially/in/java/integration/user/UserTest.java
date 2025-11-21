package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.integration.user;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.IntegrationTest;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.jayway.jsonpath.JsonPath;

import org.springframework.http.MediaType;

public class UserTest extends IntegrationTest {
    @Test
    void isAdminAdded() throws Exception {
        var reqAll =
                MockMvcRequestBuilders.get("/api/user/")
                        .queryParam("page", "0")
                        .queryParam("size", "10");
        this.mockMvc
                .perform(reqAll)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is("admin")));

        var reqOne = MockMvcRequestBuilders.get("/api/user/by-id/1");
        this.mockMvc.perform(reqOne).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("admin")));
    }

    @Test
    void canRegisterAndDeleteUser() throws Exception {
        var postReq = MockMvcRequestBuilders.post("/api/auth/register")
            .content("""
                    {"username": "someone", "plainPassword": "plain-passwrd", "role": "admin"}
                    """).contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(postReq).andExpect(MockMvcResultMatchers.status().isOk());
        
        var getReq =
                MockMvcRequestBuilders.get("/api/user/")
                        .queryParam("page", "0")
                        .queryParam("size", "10");
        
        MvcResult result = this.mockMvc
                .perform(getReq)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[-1].name", Matchers.is("someone")))
                .andReturn();
        String response = result.getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("$[-1].id");

        var deleteReq = MockMvcRequestBuilders.delete(String.format("/api/user/%d", id));
        this.mockMvc.perform(deleteReq).andExpect(MockMvcResultMatchers.status().isOk());
        
        this.mockMvc
            .perform(getReq)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)));
    }
}
