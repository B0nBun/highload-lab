package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.workplace;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.Workplace;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.Workplace.AudioEquipmentState;

public record WorkplaceResponseDTO(
    Long id,
    Long monitors,
    AudioEquipmentState audioEquipment,
    Long officeId
){
    public static WorkplaceResponseDTO fromModel(Workplace w) {
        return new WorkplaceResponseDTO(w.getId(), w.getMonitors(), w.getAudioEquipment(), w.getOffice().getId());
    }
}
