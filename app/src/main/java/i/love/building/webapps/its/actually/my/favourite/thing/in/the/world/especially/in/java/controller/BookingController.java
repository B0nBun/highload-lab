package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.controller;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.BookingInPastException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.ObjectNotFoundException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.ProblemResponseException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.meetingroom.MeetingRoomConflictException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.workplace.WorkplaceAlreadyTakenException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.workplace.WorkplaceUserAlreadyBookedException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.meetingroombooking.MeetingRoomBookingCreateRequestDTO;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.meetingroombooking.MeetingRoomBookingDTO;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.workplacebooking.WorkplaceBookingCreateRequestDTO;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.workplacebooking.WorkplaceBookingDTO;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.MeetingRoomBooking;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.WorkplaceBooking;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.service.MeetingRoomBookingService;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.service.OfficeService;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.service.UserService;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.service.WorkplaceBookingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO: Split meeting-room and workplace booking controllers

@RestController
@RequestMapping(value = "/api/booking")
public class BookingController {
    @Autowired WorkplaceBookingService workplaceBookings;

    @Autowired MeetingRoomBookingService meetingRoomBookings;

    @Autowired OfficeService office;

    @Autowired UserService users;

    @GetMapping(value = "/meeting-room/by-room/{meetingRoomId}")
    public ResponseEntity<List<MeetingRoomBookingDTO>> getMeetingRoomBookingsByRoom(
            @PathVariable @NotNull Long meetingRoomId) {
        List<MeetingRoomBooking> bookings =
                this.meetingRoomBookings.getByMeetingRoomId(meetingRoomId);
        return ResponseEntity.ok(bookings.stream().map(MeetingRoomBookingDTO::fromModel).toList());
    }

    @GetMapping(value = "/meeting-room/by-user/{userId}")
    public ResponseEntity<List<MeetingRoomBookingDTO>> getMeetingRoomBookingsByUser(
            @PathVariable @NotNull Long userId) {
        List<MeetingRoomBooking> bookings = this.meetingRoomBookings.getByUserId(userId);
        return ResponseEntity.ok(bookings.stream().map(MeetingRoomBookingDTO::fromModel).toList());
    }

    @GetMapping(value = "/meeting-room")
    public ResponseEntity<List<MeetingRoomBookingDTO>> getAllMeetingRoomBookings() {
        List<MeetingRoomBooking> bookings = this.meetingRoomBookings.getAll();
        return ResponseEntity.ok(bookings.stream().map(MeetingRoomBookingDTO::fromModel).toList());
    }

    @GetMapping(value = "/meeting-room/{bookingId}")
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

    @PostMapping(value = "/meeting-room")
    public ResponseEntity<MeetingRoomBookingDTO> createMeetingRoomBooking(
            @Valid @RequestBody MeetingRoomBookingCreateRequestDTO req) {
        try {
            MeetingRoomBooking booking =
                    this.meetingRoomBookings.create(
                            req.meetingRoomId(), req.userId(), req.startTime(), req.endTime());
            return ResponseEntity.ok(MeetingRoomBookingDTO.fromModel(booking));
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

    @DeleteMapping(value = "/meeting-room/{bookingId}")
    public ResponseEntity<Void> deleteMeetingRoomBooking(@PathVariable @NotNull Long bookingId) {
        boolean deleted = this.meetingRoomBookings.deleteById(bookingId);
        if (!deleted) {
            throw new ObjectNotFoundException("meeting room booking with id '%d'", bookingId)
                    .responseException();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/workplace/by-workplace/{workplaceId}")
    public ResponseEntity<List<WorkplaceBookingDTO>> getWorkplaceBookingsByWorkplace(
            @PathVariable @NotNull Long workplaceId) {
        List<WorkplaceBooking> bookings = this.workplaceBookings.getByWorkplaceId(workplaceId);
        return ResponseEntity.ok(bookings.stream().map(WorkplaceBookingDTO::fromModel).toList());
    }

    @GetMapping(value = "/workplace")
    public ResponseEntity<List<WorkplaceBookingDTO>> getAllWorkplaceBookings() {
        List<WorkplaceBooking> bookings = this.workplaceBookings.getAll();
        return ResponseEntity.ok(bookings.stream().map(WorkplaceBookingDTO::fromModel).toList());
    }

    @GetMapping(value = "/workplace/by-user/{userId}")
    public ResponseEntity<List<WorkplaceBookingDTO>> getWorkplaceBookingsByUser(
            @PathVariable @NotNull Long userId) {
        List<WorkplaceBooking> bookings = this.workplaceBookings.getByUserId(userId);
        return ResponseEntity.ok(bookings.stream().map(WorkplaceBookingDTO::fromModel).toList());
    }

    @GetMapping(value = "/workplace/{bookingId}")
    public ResponseEntity<WorkplaceBookingDTO> getWorkplaceBooking(
            @PathVariable @NotNull Long bookingId) {
        return this.workplaceBookings
                .getById(bookingId)
                .map(WorkplaceBookingDTO::fromModel)
                .map(ResponseEntity::ok)
                .orElseThrow(
                        () ->
                                new ObjectNotFoundException(
                                                "workplace booking with id '%d'", bookingId)
                                        .responseException());
    }

    @PostMapping(value = "/workplace")
    public ResponseEntity<WorkplaceBookingDTO> createWorkplaceBooking(
            @Valid @RequestBody WorkplaceBookingCreateRequestDTO req) {
        try {
            WorkplaceBooking booking =
                    this.workplaceBookings.create(
                            req.workplaceId(), req.userId(), req.bookedDate());
            return ResponseEntity.ok(WorkplaceBookingDTO.fromModel(booking));
        } catch (WorkplaceUserAlreadyBookedException e) {
            throw new ProblemResponseException(
                    HttpStatus.CONFLICT,
                    "user with id '%d' already has a workplace booking with date '%s' for workplace"
                            + " with id '%d'",
                    e.getUserId(),
                    e.getBookedDate().toString(),
                    e.getWorkplaceId());
        } catch (WorkplaceAlreadyTakenException e) {
            throw new ProblemResponseException(
                    HttpStatus.CONFLICT,
                    "workplace with id '%d' for '%s' is already taken by user with id '%d'",
                    e.getWorkplaceId(),
                    e.getBookedDate().toString(),
                    e.getUserId());
        } catch (BookingInPastException e) {
            throw new ProblemResponseException(
                    HttpStatus.BAD_REQUEST, "can't book workplace in the past");
        } catch (ObjectNotFoundException e) {
            throw e.responseException();
        }
    }

    @DeleteMapping(value = "/workplace/{bookingId}")
    public ResponseEntity<Void> deleteWorkplaceBooking(@PathVariable @NotNull Long bookingId) {
        boolean deleted = this.workplaceBookings.deleteById(bookingId);
        if (!deleted) {
            throw new ObjectNotFoundException("workplace booking with id '%d'", bookingId)
                    .responseException();
        }
        return ResponseEntity.ok().build();
    }
}
