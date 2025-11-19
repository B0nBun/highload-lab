package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.workplace;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.Workplace;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.Workplace.AudioEquipmentState;
import jakarta.validation.constraints.NotNull;

public record WorkplaceUpdateRequestDTO(
        @NotNull AudioEquipmentState audioEquipment, @NotNull Long monitors) {
    public Workplace updatedModel(Workplace workplace) {
        return new Workplace(
                workplace.getId(), workplace.getOffice(), this.monitors, this.audioEquipment);
    }
}
