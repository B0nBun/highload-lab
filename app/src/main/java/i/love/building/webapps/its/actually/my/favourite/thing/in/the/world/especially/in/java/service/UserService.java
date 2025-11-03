package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.User;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository users;

    public List<User> getUsers(Pageable pageable) {
        return this.users.findAll(pageable).toList();
    }

    public Optional<User> getById(Long id) {
        return this.users.findById(id);
    }

    public Optional<User> getByName(String name) {
        return this.users.findByName(name);
    }

    public User create(User user) {
        return this.users.save(user);
    }

    public boolean updateUserPasswordHash(Long id, String passwordHash) {
        int updated = this.users.updatePasswordHash(id, passwordHash);
        return updated > 0;
    }

    public boolean deleteById(Long id) {
        int updated = this.users.deleteByIdReturning(id);
        return updated > 0;
    }
}
