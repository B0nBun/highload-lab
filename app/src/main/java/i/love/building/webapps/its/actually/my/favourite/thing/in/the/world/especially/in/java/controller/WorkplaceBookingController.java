package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.controller;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.BookingInPastException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.ObjectNotFoundException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.ProblemResponseException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.workplace.WorkplaceAlreadyTakenException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.workplace.WorkplaceInaccessibleToUserException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.workplace.WorkplaceUserAlreadyBookedException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.workplacebooking.WorkplaceBookingCreateRequestDTO;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.workplacebooking.WorkplaceBookingDTO;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.WorkplaceBooking;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.service.OfficeService;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.service.UserService;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.service.WorkplaceBookingService;
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
@RequestMapping(value = "/api/booking/workplace")
public class WorkplaceBookingController {
    @Autowired WorkplaceBookingService workplaceBookings;
    @Autowired OfficeService office;
    @Autowired UserService users;

    @GetMapping(value = "/by-workplace/{workplaceId}")
    public ResponseEntity<List<WorkplaceBookingDTO>> getWorkplaceBookingsByWorkplace(
            @PathVariable @NotNull Long workplaceId) {
        List<WorkplaceBooking> bookings = this.workplaceBookings.getByWorkplaceId(workplaceId);
        return ResponseEntity.ok(bookings.stream().map(WorkplaceBookingDTO::fromModel).toList());
    }

    @GetMapping(value = "/")
    public ResponseEntity<List<WorkplaceBookingDTO>> getAllWorkplaceBookings() {
        List<WorkplaceBooking> bookings = this.workplaceBookings.getAll();
        return ResponseEntity.ok(bookings.stream().map(WorkplaceBookingDTO::fromModel).toList());
    }

    @GetMapping(value = "/by-user/{userId}")
    public ResponseEntity<List<WorkplaceBookingDTO>> getWorkplaceBookingsByUser(
            @PathVariable @NotNull Long userId) {
        List<WorkplaceBooking> bookings = this.workplaceBookings.getByUserId(userId);
        return ResponseEntity.ok(bookings.stream().map(WorkplaceBookingDTO::fromModel).toList());
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
                                                                WorkplaceBookingDTO.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "booking with specified id was not found",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
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
                                                                WorkplaceBookingDTO.class))),
                @ApiResponse(
                        responseCode = "401",
                        description = "user has no access to the workplace booking",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(
                        responseCode = "409",
                        description = "workplace has a conflicting booking",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(
                        responseCode = "400",
                        description = "trying to book the workplace in the past",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "user or workplace with specified id was not found",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    public ResponseEntity<WorkplaceBookingDTO> createWorkplaceBooking(
            @Valid @RequestBody WorkplaceBookingCreateRequestDTO req) {
        try {
            WorkplaceBooking booking =
                    this.workplaceBookings.create(
                            req.workplaceId(), req.userId(), req.bookedDate());
            return ResponseEntity.ok(WorkplaceBookingDTO.fromModel(booking));
        } catch (WorkplaceInaccessibleToUserException e) {
            throw new ProblemResponseException(
                    HttpStatus.UNAUTHORIZED,
                    "user with id '%d' can not access workplace with id '%d'",
                    e.getUserId(),
                    e.getWorkplaceId());
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

    @DeleteMapping(value = "/{bookingId}")
    @Operation(
            responses = {
                @ApiResponse(responseCode = "200"),
                @ApiResponse(
                        responseCode = "404",
                        description = "booking with specified id was not found",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
            })
    public ResponseEntity<Void> deleteWorkplaceBooking(@PathVariable @NotNull Long bookingId) {
        boolean deleted = this.workplaceBookings.deleteById(bookingId);
        if (!deleted) {
            throw new ObjectNotFoundException("workplace booking with id '%d'", bookingId)
                    .responseException();
        }
        return ResponseEntity.ok().build();
    }
}
