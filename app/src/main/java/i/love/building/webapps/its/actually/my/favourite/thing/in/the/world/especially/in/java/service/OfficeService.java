package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.service;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.AlreadyExistsException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.MeetingRoom;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.Office;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.Workplace;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.repository.OfficeRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Lazy
@Service
public class OfficeService {
    @Autowired private MeetingRoomService meetingRooms;

    @Autowired private WorkplaceService workplaces;

    @Autowired private OfficeRepository offices;

    public Optional<Office> getById(Long id) {
        return this.offices.findById(id);
    }

    @Transactional
    public Optional<OfficeDetailed> getByIdDetailed(Long id) {
        Optional<Office> office = this.offices.findById(id);
        if (office.isEmpty()) {
            return Optional.empty();
        }
        List<Workplace> workplaces = this.workplaces.getByOfficeId(id);
        List<MeetingRoom> meetingRooms = this.meetingRooms.getByOfficeId(id);
        return Optional.of(new OfficeDetailed(office.get(), workplaces, meetingRooms));
    }

    @Transactional
    public Office create(String name, byte[] map) throws AlreadyExistsException {
        Optional<Office> existing = this.offices.findByName(name);
        if (existing.isPresent()) {
            throw new AlreadyExistsException("office", existing.get().getName());
        }
        Office office = new Office(name, map);
        return this.offices.save(office);
    }

    public List<Office> getAll() {
        return this.offices.findAll();
    }

    public boolean deleteById(Long officeId) {
        return this.offices.deleteByIdReturning(officeId) > 0;
    }

    public static record OfficeDetailed(
            Office office, List<Workplace> workplaces, List<MeetingRoom> meetingRooms) {}
}
