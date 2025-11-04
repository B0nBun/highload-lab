package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.workplacebooking;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public record WorkplaceBookingCreateRequestDTO(
    @NotNull
    Long workplaceId,
    @NotNull
    Long userId,
    @NotNull
    LocalDate bookedDate
){}
