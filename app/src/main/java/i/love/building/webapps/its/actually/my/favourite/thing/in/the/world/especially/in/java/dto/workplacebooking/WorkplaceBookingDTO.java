package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.workplacebooking;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.WorkplaceBooking;
import java.time.LocalDate;

public record WorkplaceBookingDTO(Long id, Long workplaceId, Long userId, LocalDate bookedDate) {
  public static WorkplaceBookingDTO fromModel(WorkplaceBooking booking) {
    return new WorkplaceBookingDTO(
        booking.getId(),
        booking.getWorkplace().getId(),
        booking.getUser().getId(),
        booking.getBookedDate());
  }
}
