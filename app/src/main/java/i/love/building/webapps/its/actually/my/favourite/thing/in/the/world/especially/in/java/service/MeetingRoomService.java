package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.service;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.ObjectNotFoundException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.MeetingRoom;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.Office;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.repository.MeetingRoomRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MeetingRoomService {
    @Autowired private MeetingRoomRepository meetingRooms;

    @Autowired private OfficeService offices;

    public List<MeetingRoom> getAll() {
        return this.meetingRooms.findAll();
    }

    public Optional<MeetingRoom> getById(Long id) {
        return this.meetingRooms.findById(id);
    }

    public List<MeetingRoom> getByOfficeId(Long officeId) {
        return this.meetingRooms.findByOfficeId(officeId);
    }

    public boolean deleteById(Long id) {
        int updated = this.meetingRooms.deleteByIdReturning(id);
        return updated > 0;
    }

    public void deleteByOfficeId(Long officeId) {
        this.meetingRooms.deleteByOfficeId(officeId);
    }

    @Transactional
    public MeetingRoom create(Long officeId, boolean remoteAvaialable, Long capacity)
            throws ObjectNotFoundException {
        Office office =
                this.offices
                        .getById(officeId)
                        .orElseThrow(
                                () -> new ObjectNotFoundException("office with id '%d'", officeId));
        MeetingRoom room = new MeetingRoom(office, remoteAvaialable, capacity);
        return this.meetingRooms.save(room);
    }
}
