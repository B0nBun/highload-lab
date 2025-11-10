package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.office;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.Office;

public record OfficeResponseDTO(Long id, String name, byte[] map) {
    public static OfficeResponseDTO fromModel(Office office) {
        return new OfficeResponseDTO(office.getId(), office.getName(), office.getMap());
    }
}
