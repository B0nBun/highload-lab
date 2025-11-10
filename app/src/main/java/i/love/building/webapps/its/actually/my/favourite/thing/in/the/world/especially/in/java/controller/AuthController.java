package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.controller;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.AlreadyExistsException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.ProblemResponseException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.auth.LoginRequestDTO;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.auth.LoginResponseDTO;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.auth.RegisterRequestDTO;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.auth.RegisterResponseDTO;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.User;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/auth")
public class AuthController {
    @Autowired private AuthService auth;

    @PostMapping(value = "/register")
    @Operation(
            summary = "register user",
            responses = {
                @ApiResponse(
                        responseCode = "403",
                        description = "invalid request or user already exists",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(
                        responseCode = "200",
                        description = "user was registered",
                        content =
                                @Content(
                                        schema =
                                                @Schema(implementation = RegisterRequestDTO.class)))
            })
    public ResponseEntity<RegisterResponseDTO> registerUser(
            @Valid @RequestBody RegisterRequestDTO req) {
        try {
            User user = this.auth.registerUser(req.username(), req.plainPassword(), req.role());
            return ResponseEntity.ok(RegisterResponseDTO.fromModel(user));
        } catch (AlreadyExistsException ex) {
            throw new ProblemResponseException(
                    HttpStatus.CONFLICT, ex, "user '%s' already exists", ex.getIdentifier());
        }
    }

    @PostMapping(value = "/login")
    @Operation(
            summary = "sign in and authorize user",
            responses = {
                @ApiResponse(
                        responseCode = "401",
                        description = "invalid username or password",
                        content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
                @ApiResponse(
                        responseCode = "200",
                        description = "user logged in",
                        content =
                                @Content(schema = @Schema(implementation = LoginResponseDTO.class)))
            })
    public ResponseEntity<LoginResponseDTO> loginUser(@Valid @RequestBody LoginRequestDTO login) {
        Optional<User> user = this.auth.passwordHashMatch(login.username(), login.plainPassword());
        return user.map(LoginResponseDTO::fromModel)
                .map(ResponseEntity::ok)
                .orElseThrow(
                        () ->
                                new ProblemResponseException(
                                        HttpStatus.UNAUTHORIZED, "invalid username or password"));
    }
}
