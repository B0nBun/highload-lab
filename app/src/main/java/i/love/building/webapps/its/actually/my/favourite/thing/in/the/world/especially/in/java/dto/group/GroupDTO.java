package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.group;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.Group;

public record GroupDTO(
    Long id,
    String name
){
    public static GroupDTO fromModel(Group g) {
        return new GroupDTO(
            g.getId(),
            g.getName()
        );
    }
}
