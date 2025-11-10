package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.service;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.ObjectNotFoundException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.Group;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.Office;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.User;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.repository.GroupRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupService {
  @Autowired private GroupRepository groups;
  @Autowired private UserService user;
  @Autowired private OfficeService office;

  public List<Group> getAllWithoutDetails() {
    return this.groups.findAllWithoutDetails();
  }

  public Optional<Group> getByName(String name) {
    return this.groups.findByName(name);
  }

  public Optional<Group> getById(Long id) {
    return this.groups.findById(id);
  }

  public Group create(String name) {
    Group g = new Group(name);
    return this.groups.save(g);
  }

  @Transactional
  public Group addUserToGroup(Long groupId, Long userId) throws ObjectNotFoundException {
    User user =
        this.user
            .getById(userId)
            .orElseThrow(() -> new ObjectNotFoundException("user with id '%d'", userId));
    Group group = this.findGroupOrThrow(groupId);
    group.addUser(user);
    return this.groups.save(group);
  }

  @Transactional
  public Group addOfficeToGroup(Long groupId, Long officeId) throws ObjectNotFoundException {
    Group group = this.findGroupOrThrow(groupId);
    Office office =
        this.office
            .getById(officeId)
            .orElseThrow(() -> new ObjectNotFoundException("office with id '%d'", officeId));
    group.addOffice(office);
    return this.groups.save(group);
  }

  @Transactional
  public Group removeOfficeFromGroup(Long groupId, Long officeId) throws ObjectNotFoundException {
    Group group = this.findGroupOrThrow(groupId);
    group.removeOffice(officeId);
    return this.groups.save(group);
  }

  @Transactional
  public Group removeUserFromGroup(Long groupId, Long userId) throws ObjectNotFoundException {
    Group group = this.findGroupOrThrow(groupId);
    group.removeUser(userId);
    return this.groups.save(group);
  }

  public boolean delete(Long id) {
    int updated = this.groups.deleteByIdReturning(id);
    return updated > 0;
  }

  private Group findGroupOrThrow(Long groupId) throws ObjectNotFoundException {
    return this.groups
        .findById(groupId)
        .orElseThrow(() -> new ObjectNotFoundException("group with id '%d'", groupId));
  }
}
