package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.meetingroom;

import io.micrometer.common.lang.NonNull;
import jakarta.validation.constraints.Positive;

public record MeetingRoomCreateRequestDTO(
        @NonNull Boolean remoteAvailable,
        @NonNull @Positive Long capacity,
        @NonNull Long officeId) {}
