package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.meetingroom;

import io.micrometer.common.lang.NonNull;

public record MeetingRoomCreateRequestDTO(
        @NonNull Boolean remoteAvaialable, @NonNull Long capacity, @NonNull Long officeId) {}
