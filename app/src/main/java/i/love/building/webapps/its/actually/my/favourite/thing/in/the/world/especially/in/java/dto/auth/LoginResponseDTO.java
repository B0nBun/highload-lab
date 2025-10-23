package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.auth;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.User;

public record LoginResponseDTO (
    Long id,
    String username,
    User.Role role
){
    public static LoginResponseDTO fromModel(User user) {
        return new LoginResponseDTO(user.getId(), user.getName(), user.getRole());
    }
}

