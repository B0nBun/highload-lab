package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.service;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.AlreadyExistsException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.ObjectNotFoundException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.user.CannotDeleteAdminException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.User;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Value("#{environment.ADMIN_USERNAME}")
    private String adminUsername;

    @Autowired private UserRepository users;

    @Transactional
    public Pair<Long, List<User>> getUsers(Pageable pageable) {
        Long count = this.users.count();
        return Pair.of(count, this.users.findAll(pageable).toList());
    }

    public Optional<User> getById(Long id) {
        return this.users.findById(id);
    }

    public Optional<User> getByName(String name) {
        return this.users.findByName(name);
    }

    @Transactional
    public User create(User user) throws AlreadyExistsException {
        Optional<User> existing = this.users.findByName(user.getName());
        if (existing.isPresent()) {
            throw new AlreadyExistsException("user", existing.get().getName());
        }
        return this.users.save(user);
    }

    public boolean updateUserPasswordHash(Long id, String passwordHash) {
        int updated = this.users.updatePasswordHash(id, passwordHash);
        return updated > 0;
    }

    @Transactional
    public boolean deleteById(Long id) throws CannotDeleteAdminException, ObjectNotFoundException {
        boolean isAdmin =
                this.users
                        .findById(id)
                        .map(u -> u.getName() == this.adminUsername)
                        .orElseThrow(() -> new ObjectNotFoundException("user with id '%d'", id));
        if (isAdmin) {
            throw new CannotDeleteAdminException();
        }
        int updated = this.users.deleteByIdReturning(id);
        return updated > 0;
    }
}
