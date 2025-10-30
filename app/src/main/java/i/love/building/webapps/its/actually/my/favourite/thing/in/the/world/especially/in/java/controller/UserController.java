package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.Headers;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.ProblemResponseException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.common.DeleteResponseDTO;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.user.UserDTO;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {
    @Autowired
    private UserService users;

    @Value("#{environment.ADMIN_USERNAME}")
    private String adminUsername;
    
    @GetMapping(value = "/", params = { "page", "size" })
    @Operation(
        responses = {
            @ApiResponse(
                responseCode = "200",
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDTO.class)))
            )
        }
    )

    // TODO: Infinite scroll or smth from requirements?
    public ResponseEntity<List<UserDTO>> getUsers(
        @RequestParam("page") @Min(0) int page,
        @RequestParam("size") @Min(1) @Max(50) int size
    ) {
        var pageReq = PageRequest.of(page, size);
        List<UserDTO> users = this.users.getUsers(pageReq).stream().map(UserDTO::fromEntity).toList();
        return ResponseEntity
            .ok()
            .header(Headers.pageSizeHeader, String.valueOf(users.size()))
            .body(users);
    }

    @GetMapping(value = "/by-id/{userId}")
    @Operation(
        responses = {
            @ApiResponse(
                responseCode = "200",
                content = @Content(schema = @Schema(implementation = UserDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "user with specified id was not found",
                content = @Content()
            )
        }
    )
    public ResponseEntity<UserDTO> getUserById(@NotNull @PathVariable Long userId) {
        return this.users.getById(userId)
            .map(UserDTO::fromEntity)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = "/by-name/{username}")
    @Operation(
        responses = {
            @ApiResponse(
                responseCode = "200",
                content = @Content(schema = @Schema(implementation = DeleteResponseDTO.class))
            ),
            @ApiResponse(
                responseCode = "401",
                description = "can not delete the main admin user",
                content = @Content(schema = @Schema(implementation = ProblemDetail.class))
            )
        }
    )
    public ResponseEntity<DeleteResponseDTO> deleteUserByName(@NotBlank @PathVariable String username) {
        if (username == this.adminUsername) {
            throw new ProblemResponseException(HttpStatus.UNAUTHORIZED, "can't delete main admin user");
        }
        boolean deleted = this.users.deleteByName(username);
        return ResponseEntity.ok(new DeleteResponseDTO(deleted));
    }

    @GetMapping(value = "/by-name/{username}")
    @Operation(
        responses = {
            @ApiResponse(
                responseCode = "200",
                content = @Content(schema = @Schema(implementation = UserDTO.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "user by specified name was not found",
                content = @Content()
            )
        }
    )
    public ResponseEntity<UserDTO> getUserByName(@NotBlank @PathVariable String username) {
        return this.users.getByName(username)
            .map(UserDTO::fromEntity)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
