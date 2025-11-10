package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.user;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserDTO(
    @NotNull
    Long id,
    @NotBlank
    String name,
    User.Role role
){
    public static UserDTO fromModel(User u) {
        return new UserDTO(u.getId(), u.getName(), u.getRole());
    }
}
