package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.group;

import jakarta.validation.constraints.NotNull;

public record GroupAddUserRequestDTO(
    @NotNull
    Long userId
){}
