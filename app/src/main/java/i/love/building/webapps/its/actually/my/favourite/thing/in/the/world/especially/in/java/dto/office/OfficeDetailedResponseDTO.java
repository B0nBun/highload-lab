package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.office;

import java.util.List;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.meetingroom.MeetingRoomResponseDTO;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.workplace.WorkplaceResponseDTO;

public record OfficeDetailedResponseDTO(
    Long id,
    String name,
    byte[] map,
    List<MeetingRoomResponseDTO> meetingRooms,
    List<WorkplaceResponseDTO> workplaces
){}
