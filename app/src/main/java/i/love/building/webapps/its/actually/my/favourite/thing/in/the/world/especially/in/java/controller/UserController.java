package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.controller;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.Headers;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.ObjectNotFoundException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.ProblemResponseException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.user.CannotDeleteAdminException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.user.UserDTO;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.User;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {
    @Autowired private UserService users;

    // TODO: Infinite scroll or smth from requirements?
    @GetMapping(
            value = "/",
            params = {"page", "size"})
    @Operation(
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        content =
                                @Content(
                                        array =
                                                @ArraySchema(
                                                        schema =
                                                                @Schema(
                                                                        implementation =
                                                                                UserDTO.class))))
            })
    public ResponseEntity<List<UserDTO>> getUsers(
            @RequestParam("page") @Min(0) int page,
            @RequestParam("size") @Min(1) @Max(50) int size) {
        var pageReq = PageRequest.of(page, size);
        Pair<Long, List<User>> result = this.users.getUsers(pageReq);
        List<UserDTO> users = result.getSecond().stream().map(UserDTO::fromModel).toList();
        return ResponseEntity.ok()
                .header(Headers.pageSizeHeader, Long.toString(result.getFirst()))
                .body(users);
    }

    @GetMapping(value = "/by-id/{userId}")
    @Operation(
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        content = @Content(schema = @Schema(implementation = UserDTO.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "user with specified id was not found",
                        content = @Content())
            })
    public ResponseEntity<UserDTO> getUserById(@NotNull @PathVariable Long userId) {
        return this.users
                .getById(userId)
                .map(UserDTO::fromModel)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = "/{userId}")
    @Operation(
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        content = @Content(schema = @Schema(implementation = Void.class))),
                @ApiResponse(
                        responseCode = "401",
                        description = "can not delete the main admin user",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "user with specified id was not found",
                        content = @Content())
            })
    public ResponseEntity<Void> deleteUser(@NotNull @PathVariable Long userId) {
        try {
            this.users.deleteById(userId);
            return ResponseEntity.ok().build();
        } catch (CannotDeleteAdminException e) {
            throw new ProblemResponseException(
                    HttpStatus.UNAUTHORIZED, "can't delete main admin user");
        } catch (ObjectNotFoundException e) {
            throw e.responseException();
        }
    }
}
