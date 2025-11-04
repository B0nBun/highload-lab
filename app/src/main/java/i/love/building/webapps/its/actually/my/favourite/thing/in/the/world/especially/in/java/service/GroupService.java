package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.Group;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.Office;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.User;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.repository.GroupRepository;

@Service
public class GroupService {
    @Autowired
    private GroupRepository groups;

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

    public Group addUserToGroup(Group group, User user) {
        group.addUser(user);
        return this.groups.save(group);
    }

    public Group addOfficeToGroup(Group group, Office office) {
        group.addOffice(office);
        return this.groups.save(group);
    }

    public Group removeOfficeFromGroup(Group group, Long officeId) {
        group.removeOffice(officeId);
        return this.groups.save(group);
    }

    public Group removeUserFromGroup(Group group, Long userId) {
        group.removeUser(userId);
        return this.groups.save(group);
    }

    public boolean delete(Long id) {
        int updated = this.groups.deleteByIdReturning(id);
        return updated > 0;
    }
}
