package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.office;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.meetingroom.MeetingRoomResponseDTO;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.workplace.WorkplaceResponseDTO;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.service.OfficeService;
import java.util.List;

public record OfficeDetailedResponseDTO(
        Long id,
        String name,
        byte[] map,
        List<MeetingRoomResponseDTO> meetingRooms,
        List<WorkplaceResponseDTO> workplaces) {
    public static OfficeDetailedResponseDTO fromModel(OfficeService.OfficeDetailed detailed) {
        return new OfficeDetailedResponseDTO(
                detailed.office().getId(),
                detailed.office().getName(),
                detailed.office().getMap(),
                detailed.meetingRooms().stream().map(MeetingRoomResponseDTO::fromModel).toList(),
                detailed.workplaces().stream().map(WorkplaceResponseDTO::fromModel).toList());
    }
}
