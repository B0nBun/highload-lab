package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.office;

import jakarta.validation.constraints.NotBlank;

public record OfficeCreateRequestDTO(
    @NotBlank
    String name,
    byte[] map
){}
