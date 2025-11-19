package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.controller;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.ObjectNotFoundException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.meetingroom.MeetingRoomCreateRequestDTO;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.meetingroom.MeetingRoomResponseDTO;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.MeetingRoom;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.service.MeetingRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/meeting-room")
public class MeetingRoomController {
    @Autowired MeetingRoomService meetingRooms;

    @PostMapping(value = "/")
    public ResponseEntity<MeetingRoomResponseDTO> createMeetingRoom(
            @Valid @RequestBody MeetingRoomCreateRequestDTO req) {
        try {
            MeetingRoom meetingRoom =
                    this.meetingRooms.create(
                            req.officeId(), req.remoteAvaialable(), req.capacity());
            return ResponseEntity.ok(MeetingRoomResponseDTO.fromModel(meetingRoom));
        } catch (ObjectNotFoundException e) {
            throw e.responseException();
        }
    }

    @GetMapping(value = "/")
    public ResponseEntity<List<MeetingRoomResponseDTO>> getAll() {
        List<MeetingRoom> list = this.meetingRooms.getAll();
        return ResponseEntity.ok(list.stream().map(MeetingRoomResponseDTO::fromModel).toList());
    }

    @GetMapping(value = "/{meetingRoomId}")
    public ResponseEntity<MeetingRoomResponseDTO> getById(@NotNull @PathVariable Long meetingRoomId) {
        MeetingRoom meetingRoom = this.meetingRooms.getById(meetingRoomId)
            .orElseThrow(() -> new ObjectNotFoundException("meeting room with id '%d'", meetingRoomId).responseException());
        return ResponseEntity.ok(MeetingRoomResponseDTO.fromModel(meetingRoom));
    }

    @DeleteMapping(value = "/{meetingRoomId}")
    @Operation(
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        content = @Content(schema = @Schema(implementation = Void.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "meeting room with specified id was not found",
                        content = @Content())
            })
    public ResponseEntity<Void> deleteMeetingRoom(@NotNull @PathVariable Long meetingRoomId) {
        boolean deleted = this.meetingRooms.deleteById(meetingRoomId);
        if (!deleted) {
            throw new ObjectNotFoundException("meeting room with id '%d'", meetingRoomId)
                    .responseException();
        }
        return ResponseEntity.ok().build();
    }
}
