package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.BookingInPastException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.NotFoundResponseException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.meetingroom.MeetingRoomConflictException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.MeetingRoom;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.MeetingRoomBooking;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.User;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.repository.MeetingRoomBookingRepository;
import jakarta.transaction.Transactional;

@Service
public class MeetingRoomBookingService {
    @Autowired
    private MeetingRoomBookingRepository meetingRoomBookings;
    @Autowired
    private UserService users;
    @Autowired
    private OfficeService office;

    public List<MeetingRoomBooking> getByMeetingRoomId(Long meetinRoomId) {
        return this.meetingRoomBookings.findByMeetingRoomId(meetinRoomId);
    }

    public List<MeetingRoomBooking> getByUserId(Long userId) {
        return this.meetingRoomBookings.findByUserId(userId);
    }

    public Optional<MeetingRoomBooking> getById(Long id) {
        return this.meetingRoomBookings.findById(id);
    }

    public boolean deleteById(Long id) {
        int updated = this.meetingRoomBookings.deleteByIdReturning(id);
        return updated > 0;
    }

    @Transactional
    public MeetingRoomBooking create(
        Long meetingRoomId,
        Long userId,
        Instant startTime,
        Instant endTime
    ) throws BookingInPastException, MeetingRoomConflictException, NotFoundResponseException {
        Instant now = Instant.now();
        if (endTime.isBefore(now) || startTime.isBefore(now)) {
            throw new BookingInPastException();
        }
        Optional<MeetingRoomBooking> conflict = this.meetingRoomBookings.findByRoomAndOverlap(
            meetingRoomId,
            Timestamp.from(startTime),
            Timestamp.from(endTime)
        );
        if (conflict.isPresent()) {
            MeetingRoomBooking booking = conflict.get();
            throw new MeetingRoomConflictException(
                booking.getId(),
                booking.getStartTime().toInstant(),
                booking.getEndTime().toInstant()
            );
        }
        User user = this.users.getById(userId)
            .orElseThrow(() -> new NotFoundResponseException("user with id '%d'", userId));
        MeetingRoom room = this.office.getMeetingRoomById(meetingRoomId)
            .orElseThrow(() -> new NotFoundResponseException("meeting room with id '%d'", meetingRoomId));
        var booking = new MeetingRoomBooking(room, user, Timestamp.from(startTime), Timestamp.from(endTime));
        this.meetingRoomBookings.save(booking);
        return booking;
    }
}
