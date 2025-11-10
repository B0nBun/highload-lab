package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(@NotBlank String username, @NotBlank String plainPassword) {}
