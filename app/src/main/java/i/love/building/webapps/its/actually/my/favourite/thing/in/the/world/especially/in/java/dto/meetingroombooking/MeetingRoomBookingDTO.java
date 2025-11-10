package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.meetingroombooking;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.MeetingRoomBooking;
import java.time.Instant;

public record MeetingRoomBookingDTO(
    Long id, Long userId, Long meetingRoomId, Instant startTime, Instant endTime) {
  public static MeetingRoomBookingDTO fromModel(MeetingRoomBooking booking) {
    return new MeetingRoomBookingDTO(
        booking.getId(),
        booking.getUser().getId(),
        booking.getMeetingRoom().getId(),
        booking.getStartTime().toInstant(),
        booking.getEndTime().toInstant());
  }
}
