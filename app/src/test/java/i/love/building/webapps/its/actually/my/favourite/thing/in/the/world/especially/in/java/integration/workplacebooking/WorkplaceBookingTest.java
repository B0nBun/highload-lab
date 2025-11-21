package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.integration.workplacebooking;

import com.jayway.jsonpath.JsonPath;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.IntegrationTest;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.Group;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.Office;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.User;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.Workplace;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.Workplace.AudioEquipmentState;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.repository.GroupRepository;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.repository.OfficeRepository;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.repository.UserRepository;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.repository.WorkplaceRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class WorkplaceBookingTest extends IntegrationTest {
    @Autowired private OfficeRepository offices;
    @Autowired private UserRepository users;
    @Autowired private WorkplaceRepository workplaces;
    @Autowired private GroupRepository groups;

    private Group testGroup;
    private User testUser1;
    private User testUser2;
    private Workplace testWorkplace1;
    private Workplace testWorkplace2;
    private Office testOffice;

    @BeforeAll
    void setup() {
        this.testUser1 =
                this.users.save(new User("workplace-booking-user-1", "pass", User.Role.admin));
        this.testUser2 =
                this.users.save(new User("workplace-booking-user-2", "pass", User.Role.admin));
        this.testOffice = this.offices.save(new Office("workplace-booking-office", null));
        this.testWorkplace1 =
                this.workplaces.save(
                        new Workplace(this.testOffice, 1L, AudioEquipmentState.absent));
        this.testWorkplace2 =
                this.workplaces.save(
                        new Workplace(this.testOffice, 3L, AudioEquipmentState.speakers));

        this.testGroup = this.groups.save(new Group("workplace-booking-group"));
        this.testGroup.addOffice(this.testOffice);
        this.testGroup.addUser(this.testUser1);
        this.testGroup.addUser(this.testUser2);
        this.groups.save(this.testGroup);
    }

    @AfterAll
    void teardown() {
        this.groups.delete(this.testGroup);
        this.offices.delete(this.testOffice);
        this.users.delete(this.testUser1);
        this.users.delete(this.testUser2);
        this.workplaces.delete(this.testWorkplace1);
        this.workplaces.delete(this.testWorkplace2);
    }

    @Test
    void testCreateReadAndDelete() throws Exception {
        var getAllReq = MockMvcRequestBuilders.get("/api/booking/workplace/");
        this.mockMvc
                .perform(getAllReq)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));

        var createReq =
                MockMvcRequestBuilders.post("/api/booking/workplace/")
                        .content(
                                String.format(
                                        """
    {"workplaceId": %d, "userId": %d, "bookedDate": "%s"}
                    """,
                                        this.testWorkplace1.getId(),
                                        this.testUser1.getId(),
                                        this.getTomorrowBookedDate()))
                        .contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(createReq).andExpect(MockMvcResultMatchers.status().isOk());

        MvcResult result =
                this.mockMvc
                        .perform(getAllReq)
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(
                                MockMvcResultMatchers.jsonPath(
                                        "$[0].workplaceId",
                                        Matchers.is(this.testWorkplace1.getId().intValue())))
                        .andExpect(
                                MockMvcResultMatchers.jsonPath(
                                        "$[0].userId",
                                        Matchers.is(this.testUser1.getId().intValue())))
                        .andReturn();

        String response = result.getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("$[0].id");

        var getOneReq = MockMvcRequestBuilders.get(String.format("/api/booking/workplace/%d", id));
        this.mockMvc
                .perform(getOneReq)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                "$.workplaceId",
                                Matchers.is(this.testWorkplace1.getId().intValue())))
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                "$.userId", Matchers.is(this.testUser1.getId().intValue())));

        var deleteReq =
                MockMvcRequestBuilders.delete(String.format("/api/booking/workplace/%d", id));
        this.mockMvc.perform(deleteReq).andExpect(MockMvcResultMatchers.status().isOk());

        this.mockMvc
                .perform(getAllReq)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void testFilteredGet() throws Exception {
        var createReq =
                MockMvcRequestBuilders.post("/api/booking/workplace/")
                        .content(
                                String.format(
                                        """
    {"workplaceId": %d, "userId": %d, "bookedDate": "%s"}
                    """,
                                        this.testWorkplace1.getId(),
                                        this.testUser1.getId(),
                                        this.getTomorrowBookedDate()))
                        .contentType(MediaType.APPLICATION_JSON);
        var response =
                this.mockMvc
                        .perform(createReq)
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
        Integer bookingId1 = JsonPath.parse(response).read("$.id");

        createReq.content(
                String.format(
                        """
    {"workplaceId": %d, "userId": %d, "bookedDate": "%s"}
                    """,
                        this.testWorkplace2.getId(),
                        this.testUser2.getId(),
                        this.getTomorrowBookedDate()));
        response =
                this.mockMvc
                        .perform(createReq)
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
        Integer bookingId2 = JsonPath.parse(response).read("$.id");

        var getByWorkplace =
                MockMvcRequestBuilders.get(
                        String.format(
                                "/api/booking/workplace/by-workplace/%d",
                                this.testWorkplace1.getId()));
        this.mockMvc
                .perform(getByWorkplace)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                "$[0].workplaceId",
                                Matchers.is(this.testWorkplace1.getId().intValue())));

        var getByUser =
                MockMvcRequestBuilders.get(
                        String.format(
                                "/api/booking/workplace/by-user/%d",
                                this.testUser2.getId().intValue()));
        this.mockMvc
                .perform(getByUser)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                "$[0].userId", Matchers.is(this.testUser2.getId().intValue())));

        var deleteReq =
                MockMvcRequestBuilders.delete(
                        String.format("/api/booking/workplace/%d", bookingId1));
        this.mockMvc.perform(deleteReq).andExpect(MockMvcResultMatchers.status().isOk());

        deleteReq =
                MockMvcRequestBuilders.delete(
                        String.format("/api/booking/workplace/%d", bookingId2));
        this.mockMvc.perform(deleteReq).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void cantBookForUserOutsideOfOffice() throws Exception {
        User user =
                this.users.save(new User("workplace-booking-user-foo", "pass", User.Role.admin));

        var createReq =
                MockMvcRequestBuilders.post("/api/booking/workplace/")
                        .content(
                                String.format(
                                        """
    {"workplaceId": %d, "userId": %d, "bookedDate": "%s"}
                    """,
                                        this.testWorkplace1.getId(),
                                        user.getId(),
                                        this.getTomorrowBookedDate()))
                        .contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(createReq).andExpect(MockMvcResultMatchers.status().isUnauthorized());

        this.users.delete(user);
    }

    @Test
    void bookInPast() throws Exception {
        var createReq =
                MockMvcRequestBuilders.post("/api/booking/workplace/")
                        .content(
                                String.format(
                                        """
    {"workplaceId": %d, "userId": %d, "bookedDate": "%s"}
                    """,
                                        this.testWorkplace1.getId(),
                                        this.testUser1.getId(),
                                        this.getYesterdayBookedDate()))
                        .contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(createReq).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void conflictingBookings() throws Exception {
        var createReq =
                MockMvcRequestBuilders.post("/api/booking/workplace/")
                        .content(
                                String.format(
                                        """
    {"workplaceId": %d, "userId": %d, "bookedDate": "%s"}
                    """,
                                        this.testWorkplace1.getId(),
                                        this.testUser1.getId(),
                                        this.getTomorrowBookedDate()))
                        .contentType(MediaType.APPLICATION_JSON);
        String response =
                this.mockMvc
                        .perform(createReq)
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
        Integer bookingId = JsonPath.parse(response).read("$.id");

        createReq =
                MockMvcRequestBuilders.post("/api/booking/workplace/")
                        .content(
                                String.format(
                                        """
    {"workplaceId": %d, "userId": %d, "bookedDate": "%s"}
                    """,
                                        this.testWorkplace1.getId(),
                                        this.testUser2.getId(),
                                        this.getTomorrowBookedDate()))
                        .contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(createReq).andExpect(MockMvcResultMatchers.status().isConflict());

        createReq =
                MockMvcRequestBuilders.post("/api/booking/workplace/")
                        .content(
                                String.format(
                                        """
    {"workplaceId": %d, "userId": %d, "bookedDate": "%s"}
                    """,
                                        this.testWorkplace2.getId(),
                                        this.testUser1.getId(),
                                        this.getTomorrowBookedDate()))
                        .contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(createReq).andExpect(MockMvcResultMatchers.status().isConflict());

        var deleteReq =
                MockMvcRequestBuilders.delete(
                        String.format("/api/booking/workplace/%d", bookingId));
        this.mockMvc.perform(deleteReq).andExpect(MockMvcResultMatchers.status().isOk());
    }

    private String getTomorrowBookedDate() {
        LocalDate date = LocalDate.now().plusDays(1);
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    private String getYesterdayBookedDate() {
        LocalDate date = LocalDate.now().minusDays(1);
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
