package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.controller;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.BookingInPastException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.ObjectNotFoundException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.ProblemResponseException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.meetingroom.MeetingRoomConflictException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.meetingroom.MeetingRoomInaccessibleToUserException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.meetingroombooking.MeetingRoomBookingCreateRequestDTO;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.meetingroombooking.MeetingRoomBookingDTO;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.MeetingRoomBooking;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.service.MeetingRoomBookingService;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.service.OfficeService;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/booking/meeting-room")
public class MeetingRoomBookingController {

    @Autowired MeetingRoomBookingService meetingRoomBookings;

    @Autowired OfficeService office;

    @Autowired UserService users;

    @GetMapping(value = "/by-room/{meetingRoomId}")
    public ResponseEntity<List<MeetingRoomBookingDTO>> getMeetingRoomBookingsByRoom(
            @PathVariable @NotNull Long meetingRoomId) {
        List<MeetingRoomBooking> bookings =
                this.meetingRoomBookings.getByMeetingRoomId(meetingRoomId);
        return ResponseEntity.ok(bookings.stream().map(MeetingRoomBookingDTO::fromModel).toList());
    }

    @GetMapping(value = "/by-user/{userId}")
    public ResponseEntity<List<MeetingRoomBookingDTO>> getMeetingRoomBookingsByUser(
            @PathVariable @NotNull Long userId) {
        List<MeetingRoomBooking> bookings = this.meetingRoomBookings.getByUserId(userId);
        return ResponseEntity.ok(bookings.stream().map(MeetingRoomBookingDTO::fromModel).toList());
    }

    @GetMapping(value = "/")
    public ResponseEntity<List<MeetingRoomBookingDTO>> getAllMeetingRoomBookings() {
        List<MeetingRoomBooking> bookings = this.meetingRoomBookings.getAll();
        return ResponseEntity.ok(bookings.stream().map(MeetingRoomBookingDTO::fromModel).toList());
    }

    @GetMapping(value = "/{bookingId}")
    @Operation(
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        content =
                                @Content(
                                        schema =
                                                @Schema(
                                                        implementation =
                                                                MeetingRoomBookingDTO.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "booking with specified id was not found",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    public ResponseEntity<MeetingRoomBookingDTO> getMeeetingRoomBooking(
            @PathVariable @NotNull Long bookingId) {
        return this.meetingRoomBookings
                .getById(bookingId)
                .map(MeetingRoomBookingDTO::fromModel)
                .map(ResponseEntity::ok)
                .orElseThrow(
                        () ->
                                new ObjectNotFoundException(
                                                "meeting room booking with id '%d'", bookingId)
                                        .responseException());
    }

    @PostMapping(value = "/")
    @Operation(
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        content =
                                @Content(
                                        schema =
                                                @Schema(
                                                        implementation =
                                                                MeetingRoomBookingDTO.class))),
                @ApiResponse(
                        responseCode = "401",
                        description = "user has no access to the meeting room booking",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(
                        responseCode = "409",
                        description = "meeting room has a conflicting booking",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(
                        responseCode = "400",
                        description = "trying to book the room in the past",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "user or room with specified id was not found",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    public ResponseEntity<MeetingRoomBookingDTO> createMeetingRoomBooking(
            @Valid @RequestBody MeetingRoomBookingCreateRequestDTO req) {
        try {
            MeetingRoomBooking booking =
                    this.meetingRoomBookings.create(
                            req.meetingRoomId(), req.userId(), req.startTime(), req.endTime());
            return ResponseEntity.ok(MeetingRoomBookingDTO.fromModel(booking));
        } catch (MeetingRoomInaccessibleToUserException e) {
            throw new ProblemResponseException(
                    HttpStatus.UNAUTHORIZED,
                    "user with id '%d' has no access to meeting room with id '%d'",
                    e.getUserId(),
                    e.getMeetingRoomId());
        } catch (MeetingRoomConflictException e) {
            throw new ProblemResponseException(
                    HttpStatus.CONFLICT,
                    "meeting room with id '%d' already has a booking from '%s' to '%s'",
                    e.getMeetingRoomId(),
                    e.getStartTime().toString(),
                    e.getEndTime().toString());
        } catch (BookingInPastException e) {
            throw new ProblemResponseException(
                    HttpStatus.BAD_REQUEST, "can't booking meeting room in the past");
        } catch (ObjectNotFoundException e) {
            throw e.responseException();
        }
    }

    @DeleteMapping(value = "/{bookingId}")
    @Operation(
            responses = {
                @ApiResponse(responseCode = "200"),
                @ApiResponse(
                        responseCode = "404",
                        description = "booking with specified id was not found",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    public ResponseEntity<Void> deleteMeetingRoomBooking(@PathVariable @NotNull Long bookingId) {
        boolean deleted = this.meetingRoomBookings.deleteById(bookingId);
        if (!deleted) {
            throw new ObjectNotFoundException("meeting room booking with id '%d'", bookingId)
                    .responseException();
        }
        return ResponseEntity.ok().build();
    }
}
