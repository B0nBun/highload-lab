package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.controller;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.ObjectNotFoundException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.workplace.WorkplaceCreateRequestDTO;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.workplace.WorkplaceResponseDTO;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.workplace.WorkplaceUpdateRequestDTO;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.Workplace;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.service.WorkplaceService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/workplace")
public class WorkplaceController {
    @Autowired private WorkplaceService workplaces;

    @PostMapping(value = "/")
    public ResponseEntity<WorkplaceResponseDTO> createWorkplace(
            @Valid @RequestBody WorkplaceCreateRequestDTO req) {
        try {
            Workplace workplace =
                    this.workplaces.create(req.officeId(), req.monitors(), req.audioEquipment());
            return ResponseEntity.ok(WorkplaceResponseDTO.fromModel(workplace));
        } catch (ObjectNotFoundException e) {
            throw e.responseException();
        }
    }

    @GetMapping(value = "/")
    public ResponseEntity<List<WorkplaceResponseDTO>> getAll() {
        List<Workplace> workplaces = this.workplaces.getAll();
        return ResponseEntity.ok(workplaces.stream().map(WorkplaceResponseDTO::fromModel).toList());
    }

    @GetMapping(value = "/{workplaceId}")
    public ResponseEntity<WorkplaceResponseDTO> getById(@NotNull @PathVariable Long workplaceId) {
        Workplace workplace =
                this.workplaces
                        .getById(workplaceId)
                        .orElseThrow(
                                () ->
                                        new ObjectNotFoundException(
                                                        "workplace with id '%d'", workplaceId)
                                                .responseException());
        return ResponseEntity.ok(WorkplaceResponseDTO.fromModel(workplace));
    }

    @PutMapping(value = "/{workplaceId}")
    public ResponseEntity<WorkplaceResponseDTO> update(
        @NotNull @PathVariable Long workplaceId,
        @RequestBody @Valid WorkplaceUpdateRequestDTO req
    ) {
        try {
            Workplace workplace = this.workplaces.update(workplaceId, req);
            return ResponseEntity.ok(WorkplaceResponseDTO.fromModel(workplace));
        } catch (ObjectNotFoundException e) {
            throw e.responseException();
        }
    }

    @DeleteMapping(value = "/{workplaceId}")
    @Operation(
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        content = @Content(schema = @Schema(implementation = Void.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "workplace with specified id was not found",
                        content = @Content())
            })
    public ResponseEntity<Void> deleteWorkplace(@NotNull @PathVariable Long workplaceId) {
        boolean deleted = this.workplaces.deleteById(workplaceId);
        if (!deleted) {
            throw new ObjectNotFoundException("workplace with id '%d'", workplaceId)
                    .responseException();
        }
        return ResponseEntity.ok().build();
    }
}
