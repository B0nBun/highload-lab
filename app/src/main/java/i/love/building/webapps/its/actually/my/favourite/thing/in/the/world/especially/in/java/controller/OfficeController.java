package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.controller;

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

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.AlreadyExistsException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.ProblemResponseException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.auth.RegisterRequestDTO;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.meetingroom.MeetingRoomCreateRequestDTO;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.meetingroom.MeetingRoomResponseDTO;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.office.OfficeCreateRequestDTO;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.office.OfficeDTO;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.office.OfficeDetailedResponseDTO;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.office.OfficeResponseDTO;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.workplace.WorkplaceCreateRequestDTO;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.workplace.WorkplaceResponseDTO;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.MeetingRoom;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.Office;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.Workplace;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.service.OfficeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping(value = "/api/office")
public class OfficeController {
    @Autowired
    private OfficeService offices;

    @GetMapping(value = "/{officeId}")
    @Operation(
        summary = "get office with all meeting rooms and workplaces",
        responses = {
            @ApiResponse(
                responseCode = "404",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                responseCode = "200",
                content = @Content(schema = @Schema(implementation = OfficeDetailedResponseDTO.class))
            )
        }
    )
    public ResponseEntity<OfficeDetailedResponseDTO> getOffice(@NotNull @PathVariable Long officeId) {
        // TODO: Transaction
        Office office = this.offices.getById(officeId)
            .orElseThrow(() -> new ProblemResponseException(HttpStatus.NOT_FOUND, "office with id %d not found", officeId));
        List<WorkplaceResponseDTO> workplaces = this.offices.getWorkplacesByOfficeId(officeId).stream()
            .map(WorkplaceResponseDTO::fromModel)
            .toList();
        List<MeetingRoomResponseDTO> meetingRooms = this.offices.getMeetingRoomsByOfficeId(officeId).stream()
            .map(MeetingRoomResponseDTO::fromModel)
            .toList();
        var response = new OfficeDetailedResponseDTO(officeId, office.getName(), office.getMap(), meetingRooms, workplaces);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/")
    public ResponseEntity<List<OfficeDTO>> getAllOffices() {
        List<OfficeDTO> list = this.offices.getAll().stream().map(OfficeDTO::fromEntity).toList();
        return ResponseEntity.ok(list);
    }
    
    @PostMapping(value = "/")
    @Operation(
        summary = "create new office",
        responses = {
            @ApiResponse(
                responseCode = "403",
                description="invalid request or office with name already exists",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            ),
            @ApiResponse(
                responseCode = "200",
                content = @Content(schema = @Schema(implementation = RegisterRequestDTO.class))
            )
        }
    )
    public ResponseEntity<OfficeResponseDTO> createOffice(@Valid @RequestBody OfficeCreateRequestDTO req) {
        try {
            Office office = this.offices.create(req.name(), req.map());
            return ResponseEntity.ok(OfficeResponseDTO.fromModel(office));
        } catch (AlreadyExistsException ex) {
            throw new ProblemResponseException(
                HttpStatus.BAD_REQUEST, 
                ex, 
                "office '%s' already exists",
                ex.getIdentifier()
            );
        }
    }

    @PostMapping(value = "/{officeId}/workplace")
    public ResponseEntity<WorkplaceResponseDTO> createWorkplace(
        @NotNull @PathVariable Long officeId,
        @Valid @RequestBody WorkplaceCreateRequestDTO req
    ) {
        Office office = this.offices.getById(officeId)
            .orElseThrow(
                () -> new ProblemResponseException(
                    HttpStatus.BAD_REQUEST,
                    "office with id %d does not exist", officeId
                )
            );
        Workplace workplace = this.offices.createWorkplace(office, req.monitors(), req.audioEquipment());
        return ResponseEntity.ok(WorkplaceResponseDTO.fromModel(workplace));
    }

    @PostMapping(value = "/{officeId}/meeting-room")
    public ResponseEntity<MeetingRoomResponseDTO> createMeetingRoom(
        @NotNull @PathVariable Long officeId,
        @Valid @RequestBody MeetingRoomCreateRequestDTO req
    ) {
        Office office = this.offices.getById(officeId)
            .orElseThrow(
                () -> new ProblemResponseException(
                    HttpStatus.BAD_REQUEST,
                    "office with id %d does not exist", officeId
                )
            );
        MeetingRoom meetingRoom = this.offices.createMeetingRoom(office, req.remoteAvaialable(), req.capacity());
        return ResponseEntity.ok(MeetingRoomResponseDTO.fromModel(meetingRoom));
    }

    @DeleteMapping(value = "/{officeId}")
    @Operation(
        responses = {
            @ApiResponse(
                responseCode = "200",
                content = @Content(schema = @Schema(implementation = Void.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "office with specified id was not found",
                content = @Content()
            )
        }
    )
    public ResponseEntity<Void> deleteOffice(@NotNull @PathVariable Long officeId) {
        // TODO: Transaction
        this.offices.deleteMeetingRoomByOfficeId(officeId);
        this.offices.deleteWorkplacesByOfficeId(officeId);
        boolean deleted = this.offices.deleteOffice(officeId);
        if (!deleted) {
            throw new ProblemResponseException(HttpStatus.NOT_FOUND, "office with id '%s' not found", officeId);
        }
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping(value = "/workplace/{workplaceId}")
    @Operation(
        responses = {
            @ApiResponse(
                responseCode = "200",
                content = @Content(schema = @Schema(implementation = Void.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "workplace with specified id was not found",
                content = @Content()
            )
        }
    )
    public ResponseEntity<Void> deleteWorkplace(@NotNull @PathVariable Long workplaceId) {
        boolean deleted = this.offices.deleteWorkplace(workplaceId);
        if (!deleted) {
            throw new ProblemResponseException(HttpStatus.NOT_FOUND, "workplace with id '%s' not found", workplaceId);
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/meeting-room/{meetingRoomId}")
    @Operation(
        responses = {
            @ApiResponse(
                responseCode = "200",
                content = @Content(schema = @Schema(implementation = Void.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "meeting room with specified id was not found",
                content = @Content()
            )
        }
    )
    public ResponseEntity<Void> deleteMeetingRoom(@NotNull @PathVariable Long meetingRoomId) {
        boolean deleted = this.offices.deleteMeetingRoom(meetingRoomId);
        if (!deleted) {
            throw new ProblemResponseException(HttpStatus.NOT_FOUND, "meeting room with id '%s' not found", meetingRoomId);
        }
        return ResponseEntity.ok().build();
    }
}
