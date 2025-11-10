package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.service;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.ObjectNotFoundException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.Office;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.Workplace;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.Workplace.AudioEquipmentState;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.repository.WorkplaceRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkplaceService {
  @Autowired private WorkplaceRepository workplaces;

  @Autowired private OfficeService offices;

  public List<Workplace> getAll() {
    return this.workplaces.findAll();
  }

  public Optional<Workplace> getById(Long id) {
    return this.workplaces.findById(id);
  }

  public List<Workplace> getByOfficeId(Long officeId) {
    return this.workplaces.findByOfficeId(officeId);
  }

  public boolean deleteById(Long id) {
    int updated = this.workplaces.deleteByIdReturning(id);
    return updated > 0;
  }

  public void deleteByOfficeId(Long officeId) {
    this.workplaces.deleteByOfficeId(officeId);
  }

  @Transactional
  public Workplace create(Long officeId, Long monitors, AudioEquipmentState audio)
      throws ObjectNotFoundException {
    Office office =
        this.offices
            .getById(officeId)
            .orElseThrow(() -> new ObjectNotFoundException("office with id '%d'", officeId));
    Workplace workplace = new Workplace(office, monitors, audio);
    return this.workplaces.save(workplace);
  }
}
