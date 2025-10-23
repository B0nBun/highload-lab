package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.auth;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.User;

public record RegisterResponseDTO (
    Long id,
    String username,
    User.Role role
){
    public static RegisterResponseDTO fromModel(User user) {
        return new RegisterResponseDTO(user.getId(), user.getName(), user.getRole());
    }
}
