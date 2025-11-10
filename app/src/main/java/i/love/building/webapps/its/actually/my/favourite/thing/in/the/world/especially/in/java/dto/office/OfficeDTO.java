package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.office;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.Office;

public record OfficeDTO(Long id, String name, byte[] map) {
  public static OfficeDTO fromModel(Office o) {
    return new OfficeDTO(o.getId(), o.getName(), o.getMap());
  }
}
