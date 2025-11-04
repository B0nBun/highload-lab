package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.BookingInPastException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.workplace.WorkplaceAlreadyTakenException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.workplace.WorkplaceUserAlreadyBookedException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.User;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.Workplace;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.WorkplaceBooking;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.repository.WorkplaceBookingRepository;

@Service
public class WorkplaceBookingService {
    @Autowired
    private WorkplaceBookingRepository workplaceBookings;

    public List<WorkplaceBooking> getByWorkplaceId(Long workplaceId) {
        return this.workplaceBookings.findByWorkplaceId(workplaceId);
    }

    public List<WorkplaceBooking> getByUserId(Long userId) {
        return this.workplaceBookings.findByUserId(userId);
    }

    public Optional<WorkplaceBooking> getById(Long id) {
        return this.workplaceBookings.findById(id);
    }

    public boolean deleteById(Long id) {
        int updated = this.workplaceBookings.deleteByIdReturning(id);
        return updated > 0;
    }

    public WorkplaceBooking create(
        Workplace workplace,
        User user,
        LocalDate bookedDate
    ) throws WorkplaceUserAlreadyBookedException, BookingInPastException, WorkplaceAlreadyTakenException {
        if (bookedDate.isBefore(LocalDate.now())) {
            throw new BookingInPastException();
        }
        Optional<WorkplaceBooking> conflict = this.workplaceBookings.findByUserIdAndDate(user.getId(), bookedDate);
        if (conflict.isPresent()) {
            WorkplaceBooking booking = conflict.get();
            throw new WorkplaceUserAlreadyBookedException(
                booking.getBookedDate(),
                booking.getUser().getId(),
                booking.getWorkplace().getId()
            );
        }
        conflict = this.workplaceBookings.findByWorkplaceAndDate(workplace.getId(), bookedDate);
        if (conflict.isPresent()) {
            throw new WorkplaceAlreadyTakenException(bookedDate, conflict.get().getUser().getId(), workplace.getId());
        }
        var booking = new WorkplaceBooking(workplace, user, bookedDate);
        this.workplaceBookings.save(booking);
        return booking;
    }
}
