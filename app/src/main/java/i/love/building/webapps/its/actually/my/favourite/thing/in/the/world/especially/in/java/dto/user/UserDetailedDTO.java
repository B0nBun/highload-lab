package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.user;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record UserDetailedDTO(
        @NotNull Long id, @NotBlank String name, User.Role role, List<String> groups) {}
