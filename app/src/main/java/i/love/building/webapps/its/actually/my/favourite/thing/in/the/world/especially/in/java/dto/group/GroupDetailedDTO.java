package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.group;

import java.util.List;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.Group;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.Office;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.User;

public record GroupDetailedDTO(
    Long id,
    String name,
    List<Long> userIds,
    List<Long> officeIds
){
    public static GroupDetailedDTO fromEntity(Group g) {
        return new GroupDetailedDTO(
            g.getId(),
            g.getName(),
            g.getUsers().stream().map(User::getId).toList(),
            g.getOffices().stream().map(Office::getId).toList()
        );
    }
}
