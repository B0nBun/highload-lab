package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.service;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.AlreadyExistsException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.ObjectNotFoundException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.MeetingRoom;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.Office;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.Workplace;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.Workplace.AudioEquipmentState;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.repository.MeetingRoomRepository;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.repository.OfficeRepository;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.repository.WorkplaceRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OfficeService {
  @Autowired private MeetingRoomRepository meetingRooms;

  @Autowired private WorkplaceRepository workplaces;

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
    List<Workplace> workplaces = this.workplaces.findByOfficeId(id);
    List<MeetingRoom> meetingRooms = this.meetingRooms.findByOfficeId(id);
    return Optional.of(new OfficeDetailed(office.get(), workplaces, meetingRooms));
  }

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

  public Optional<Workplace> getWorkplaceById(Long workplaceId) {
    return this.workplaces.findById(workplaceId);
  }

  public Optional<MeetingRoom> getMeetingRoomById(Long meetingRoomId) {
    return this.meetingRooms.findById(meetingRoomId);
  }

  public List<Workplace> getWorkplacesByOfficeId(Long officeId) {
    return this.workplaces.findByOfficeId(officeId);
  }

  public List<MeetingRoom> getMeetingRoomsByOfficeId(Long officeId) {
    return this.meetingRooms.findByOfficeId(officeId);
  }

  @Transactional
  public Workplace createWorkplace(Long officeId, Long monitors, AudioEquipmentState audioEquipment)
      throws ObjectNotFoundException {
    Office office =
        this.offices
            .findById(officeId)
            .orElseThrow(() -> new ObjectNotFoundException("office with id '%d'", officeId));
    var workplace = new Workplace(office, monitors, audioEquipment);
    return this.workplaces.save(workplace);
  }

  @Transactional
  public MeetingRoom createMeetingRoom(Long officeId, boolean remoteAvaialable, Long capacity)
      throws ObjectNotFoundException {
    Office office =
        this.offices
            .findById(officeId)
            .orElseThrow(() -> new ObjectNotFoundException("office with id '%d'", officeId));
    var meetingRoom = new MeetingRoom(office, remoteAvaialable, capacity);
    return this.meetingRooms.save(meetingRoom);
  }

  public boolean deleteWorkplace(Long workplaceId) {
    return this.workplaces.deleteByIdReturning(workplaceId) > 0;
  }

  public boolean deleteMeetingRoom(Long meetingRoomId) {
    return this.meetingRooms.deleteByIdReturning(meetingRoomId) > 0;
  }

  public boolean deleteOffice(Long officeId) {
    this.meetingRooms.deleteByOfficeId(officeId);
    this.workplaces.deleteByOfficeId(officeId);
    return this.offices.deleteByIdReturning(officeId) > 0;
  }

  public static record OfficeDetailed(
      Office office, List<Workplace> workplaces, List<MeetingRoom> meetingRooms) {}
}
