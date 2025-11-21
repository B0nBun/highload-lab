package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.service;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.BookingInPastException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.ObjectNotFoundException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.meetingroom.MeetingRoomConflictException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.meetingroom.MeetingRoomInaccessibleToUserException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.MeetingRoom;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.MeetingRoomBooking;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.User;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.repository.MeetingRoomBookingRepository;
import jakarta.transaction.Transactional;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MeetingRoomBookingService {
    @Autowired private MeetingRoomBookingRepository meetingRoomBookings;
    @Autowired private UserService users;
    @Autowired private MeetingRoomService meetingRooms;
    @Autowired private GroupService groups;

    public List<MeetingRoomBooking> getByMeetingRoomId(Long meetinRoomId) {
        return this.meetingRoomBookings.findByMeetingRoomId(meetinRoomId);
    }

    public List<MeetingRoomBooking> getAll() {
        return this.meetingRoomBookings.findAll();
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
            Long meetingRoomId, Long userId, Instant startTime, Instant endTime)
            throws BookingInPastException,
                    MeetingRoomConflictException,
                    ObjectNotFoundException,
                    MeetingRoomInaccessibleToUserException {
        Instant now = Instant.now();
        if (endTime.isBefore(now) || startTime.isBefore(now)) {
            throw new BookingInPastException();
        }

        User user =
                this.users
                        .getById(userId)
                        .orElseThrow(
                                () -> new ObjectNotFoundException("user with id '%d'", userId));
        if (user.getRole() == User.Role.regular || user.getRole() == User.Role.infrequent) {
            throw new MeetingRoomInaccessibleToUserException(userId, meetingRoomId);
        }

        MeetingRoom room =
                this.meetingRooms
                        .getById(meetingRoomId)
                        .orElseThrow(
                                () ->
                                        new MeetingRoomInaccessibleToUserException(
                                                userId, meetingRoomId));
        if (!this.groups.userHasAccessToOffice(userId, room.getOffice().getId())) {
            throw new MeetingRoomInaccessibleToUserException(userId, meetingRoomId);
        }
        Optional<MeetingRoomBooking> conflict =
                this.meetingRoomBookings.findByRoomAndOverlap(
                        meetingRoomId, Timestamp.from(startTime), Timestamp.from(endTime));
        if (conflict.isPresent()) {
            MeetingRoomBooking booking = conflict.get();
            throw new MeetingRoomConflictException(
                    booking.getId(),
                    booking.getStartTime().toInstant(),
                    booking.getEndTime().toInstant());
        }
        var booking =
                new MeetingRoomBooking(
                        room, user, Timestamp.from(startTime), Timestamp.from(endTime));
        this.meetingRoomBookings.save(booking);
        return booking;
    }
}
