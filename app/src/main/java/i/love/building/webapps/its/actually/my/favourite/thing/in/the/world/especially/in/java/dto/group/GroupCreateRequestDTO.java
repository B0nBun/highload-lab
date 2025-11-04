package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.group;

import jakarta.validation.constraints.NotBlank;

public record GroupCreateRequestDTO(
    @NotBlank
    String name
) {}
