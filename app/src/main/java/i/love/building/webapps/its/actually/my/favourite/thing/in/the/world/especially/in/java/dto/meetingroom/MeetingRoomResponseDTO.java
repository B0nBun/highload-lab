package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.meetingroom;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.MeetingRoom;

public record MeetingRoomResponseDTO(
    Long id, Long officeId, boolean remoteAvaialable, Long capacity) {
  public static MeetingRoomResponseDTO fromModel(MeetingRoom r) {
    return new MeetingRoomResponseDTO(
        r.getId(), r.getOffice().getId(), r.getRemoteAvailable(), r.getCapacity());
  }
}
