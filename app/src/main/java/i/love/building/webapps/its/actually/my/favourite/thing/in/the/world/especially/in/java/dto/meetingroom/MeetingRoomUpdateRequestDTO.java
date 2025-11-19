package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.meetingroom;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.MeetingRoom;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MeetingRoomUpdateRequestDTO(
        @NotNull Boolean remoteAvailable, @NotNull @Positive Long capacity) {
    public MeetingRoom updatedModel(MeetingRoom room) {
        return new MeetingRoom(room.getId(), room.getOffice(), this.remoteAvailable, this.capacity);
    }
}
