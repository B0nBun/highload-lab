package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.user;

import java.util.List;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserDetailedDTO(
    @NotNull
    Long id,
    @NotBlank
    String name,
    User.Role role,
    List<String> groups
){}
