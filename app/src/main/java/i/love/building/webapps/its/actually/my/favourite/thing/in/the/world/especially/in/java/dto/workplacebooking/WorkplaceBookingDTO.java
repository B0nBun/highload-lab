package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.workplacebooking;

import java.time.LocalDate;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.WorkplaceBooking;

public record WorkplaceBookingDTO(
    Long id,
    Long workplaceId,
    Long userId,
    LocalDate bookedDate
){
    public static WorkplaceBookingDTO fromEntity(WorkplaceBooking booking) {
        return new WorkplaceBookingDTO(
            booking.getId(),
            booking.getWorkplace().getId(),
            booking.getUser().getId(),
            booking.getBookedDate()
        );
    }
}
