package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.controller;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.AlreadyExistsException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.ObjectNotFoundException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.ProblemResponseException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.auth.RegisterRequestDTO;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.office.OfficeCreateRequestDTO;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.office.OfficeDTO;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.office.OfficeDetailedResponseDTO;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.office.OfficeResponseDTO;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.Office;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.service.OfficeService;
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
@RequestMapping(value = "/api/office")
public class OfficeController {
    @Autowired private OfficeService offices;

    @GetMapping(value = "/{officeId}")
    @Operation(
            summary = "get office with all meeting rooms and workplaces",
            responses = {
                @ApiResponse(
                        responseCode = "404",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(
                        responseCode = "200",
                        content =
                                @Content(
                                        schema =
                                                @Schema(
                                                        implementation =
                                                                OfficeDetailedResponseDTO.class)))
            })
    public ResponseEntity<OfficeDetailedResponseDTO> getOffice(
            @NotNull @PathVariable Long officeId) {
        return this.offices
                .getByIdDetailed(officeId)
                .map(OfficeDetailedResponseDTO::fromModel)
                .map(ResponseEntity::ok)
                .orElseThrow(
                        () ->
                                new ObjectNotFoundException("office with id '%d'", officeId)
                                        .responseException());
    }

    @GetMapping(value = "/")
    public ResponseEntity<List<OfficeDTO>> getAllOffices() {
        List<OfficeDTO> list = this.offices.getAll().stream().map(OfficeDTO::fromModel).toList();
        return ResponseEntity.ok(list);
    }

    @PostMapping(value = "/")
    @Operation(
            summary = "create new office",
            responses = {
                @ApiResponse(
                        responseCode = "403",
                        description = "invalid request or office with name already exists",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(
                        responseCode = "200",
                        content =
                                @Content(
                                        schema =
                                                @Schema(implementation = RegisterRequestDTO.class)))
            })
    public ResponseEntity<OfficeResponseDTO> createOffice(
            @Valid @RequestBody OfficeCreateRequestDTO req) {
        try {
            Office office = this.offices.create(req.name(), req.map());
            return ResponseEntity.ok(OfficeResponseDTO.fromModel(office));
        } catch (AlreadyExistsException ex) {
            throw new ProblemResponseException(
                    HttpStatus.BAD_REQUEST, ex, "office '%s' already exists", ex.getIdentifier());
        }
    }

    @DeleteMapping(value = "/{officeId}")
    @Operation(
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        content = @Content(schema = @Schema(implementation = Void.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "office with specified id was not found",
                        content = @Content())
            })
    public ResponseEntity<Void> deleteOffice(@NotNull @PathVariable Long officeId) {
        boolean deleted = this.offices.deleteById(officeId);
        if (!deleted) {
            throw new ObjectNotFoundException("office with id '%d'", officeId).responseException();
        }
        return ResponseEntity.ok().build();
    }
}
