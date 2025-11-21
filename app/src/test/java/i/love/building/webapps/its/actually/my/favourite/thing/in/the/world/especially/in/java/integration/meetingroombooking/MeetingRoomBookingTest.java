package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.integration.meetingroombooking;

import com.jayway.jsonpath.JsonPath;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.IntegrationTest;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.Group;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.MeetingRoom;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.Office;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.User;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.repository.GroupRepository;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.repository.MeetingRoomRepository;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.repository.OfficeRepository;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.repository.UserRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class MeetingRoomBookingTest extends IntegrationTest {
    @Autowired private OfficeRepository offices;
    @Autowired private UserRepository users;
    @Autowired private MeetingRoomRepository meetingRooms;
    @Autowired private GroupRepository groups;

    private Group testGroup;
    private User testUser1;
    private User testUser2;
    private MeetingRoom testMeetingRoom1;
    private MeetingRoom testMeetingRoom2;
    private Office testOffice;

    @BeforeAll
    void setup() {
        this.testUser1 =
                this.users.save(new User("meeting-room-booking-user-1", "pass", User.Role.admin));
        this.testUser2 =
                this.users.save(new User("meeting-room-booking-user-2", "pass", User.Role.admin));
        this.testOffice = this.offices.save(new Office("meeting-room-booking-office", null));
        this.testMeetingRoom1 = this.meetingRooms.save(new MeetingRoom(this.testOffice, false, 1L));
        this.testMeetingRoom2 = this.meetingRooms.save(new MeetingRoom(this.testOffice, true, 2L));

        this.testGroup = this.groups.save(new Group("meeting-room-booking-group"));
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
        this.meetingRooms.delete(this.testMeetingRoom1);
        this.meetingRooms.delete(this.testMeetingRoom2);
    }

    @Test
    void testCreateReadAndDelete() throws Exception {
        var getAllReq = MockMvcRequestBuilders.get("/api/booking/meeting-room/");
        this.mockMvc
                .perform(getAllReq)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));

        var createReq =
                MockMvcRequestBuilders.post("/api/booking/meeting-room/")
                        .content(
                                String.format(
                                        """
    {"meetingRoomId": %d, "userId": %d, "startTime": "%s", "endTime": "%s"}
                    """,
                                        this.testMeetingRoom1.getId(),
                                        this.testUser1.getId(),
                                        this.getTomorrowStartTime(),
                                        this.getTomorrowEndTime()))
                        .contentType(MediaType.APPLICATION_JSON);
        this.mockMvc.perform(createReq).andExpect(MockMvcResultMatchers.status().isOk());

        MvcResult result =
                this.mockMvc
                        .perform(getAllReq)
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andExpect(
                                MockMvcResultMatchers.jsonPath(
                                        "$[0].meetingRoomId",
                                        Matchers.is(this.testMeetingRoom1.getId().intValue())))
                        .andExpect(
                                MockMvcResultMatchers.jsonPath(
                                        "$[0].userId",
                                        Matchers.is(this.testUser1.getId().intValue())))
                        .andReturn();

        String response = result.getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("$[0].id");

        var getOneReq =
                MockMvcRequestBuilders.get(String.format("/api/booking/meeting-room/%d", id));
        this.mockMvc
                .perform(getOneReq)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                "$.meetingRoomId",
                                Matchers.is(this.testMeetingRoom1.getId().intValue())))
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                "$.userId", Matchers.is(this.testUser1.getId().intValue())));

        var deleteReq =
                MockMvcRequestBuilders.delete(String.format("/api/booking/meeting-room/%d", id));
        this.mockMvc.perform(deleteReq).andExpect(MockMvcResultMatchers.status().isOk());

        this.mockMvc
                .perform(getAllReq)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void testFilteredGet() throws Exception {
        var createReq =
                MockMvcRequestBuilders.post("/api/booking/meeting-room/")
                        .content(
                                String.format(
                                        """
    {"meetingRoomId": %d, "userId": %d, "startTime": "%s", "endTime": "%s"}
                    """,
                                        this.testMeetingRoom1.getId(),
                                        this.testUser1.getId(),
                                        this.getTomorrowStartTime(),
                                        this.getTomorrowStartTime()))
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
    {"meetingRoomId": %d, "userId": %d, "startTime": "%s", "endTime": "%s"}
                    """,
                        this.testMeetingRoom2.getId(),
                        this.testUser2.getId(),
                        this.getTomorrowStartTime(),
                        this.getTomorrowEndTime()));
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
                                "/api/booking/meeting-room/by-room/%d",
                                this.testMeetingRoom1.getId()));
        this.mockMvc
                .perform(getByWorkplace)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(
                        MockMvcResultMatchers.jsonPath(
                                "$[0].meetingRoomId",
                                Matchers.is(this.testMeetingRoom1.getId().intValue())));

        var getByUser =
                MockMvcRequestBuilders.get(
                        String.format(
                                "/api/booking/meeting-room/by-user/%d",
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
                        String.format("/api/booking/meeting-room/%d", bookingId1));
        this.mockMvc.perform(deleteReq).andExpect(MockMvcResultMatchers.status().isOk());

        deleteReq =
                MockMvcRequestBuilders.delete(
                        String.format("/api/booking/meeting-room/%d", bookingId2));
        this.mockMvc.perform(deleteReq).andExpect(MockMvcResultMatchers.status().isOk());
    }

    private String getTomorrowStartTime() {
        Instant startTime = Instant.now().plus(1, ChronoUnit.DAYS);
        return startTime.toString();
    }

    private String getTomorrowEndTime() {
        Instant startTime = Instant.now().plus(1, ChronoUnit.DAYS).plus(1, ChronoUnit.HOURS);
        return startTime.toString();
    }
}
