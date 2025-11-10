package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.auth;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.User;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequestDTO(
    @NotBlank String username, @NotBlank String plainPassword, User.Role role) {}
