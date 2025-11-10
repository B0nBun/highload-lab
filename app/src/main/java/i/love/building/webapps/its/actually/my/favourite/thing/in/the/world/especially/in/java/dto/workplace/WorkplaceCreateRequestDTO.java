package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.workplace;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.Workplace.AudioEquipmentState;
import jakarta.validation.constraints.NotNull;

public record WorkplaceCreateRequestDTO(
    @NotNull Long monitors, @NotNull AudioEquipmentState audioEquipment, @NotNull Long officeId) {}
