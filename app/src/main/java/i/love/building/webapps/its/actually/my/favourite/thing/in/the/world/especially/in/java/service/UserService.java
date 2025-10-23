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
    private UserRepository repo;    

    public List<User> getUsers(Pageable pageable) {
        return this.repo.findAll(pageable).toList();
    }

    public Optional<User> getById(Long id) {
        return this.repo.findById(id);
    }

    public Optional<User> getByName(String name) {
        return this.repo.findByName(name);
    }

    public User create(User user) {
        return this.repo.save(user);
    }

    public boolean updateUserPasswordHash(Long id, String passwordHash) {
        int updated = this.repo.updatePasswordHash(id, passwordHash);
        return updated > 0;
    }

    public boolean deleteById(Long id) {
        int updated = this.repo.deleteByIdReturning(id);
        return updated > 0;
    }

    public boolean deleteByName(String name) {
        int updated = this.repo.deleteByName(name);
        return updated > 0;
    }
}
