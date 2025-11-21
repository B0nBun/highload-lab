package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.service;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.AlreadyExistsException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.User;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserService users;

    @Autowired
    public AuthService(final UserService users) {
        this.users = users;
    }

    public User registerUser(String name, String plainPassword, User.Role role)
            throws AlreadyExistsException {
        String salt = BCrypt.gensalt();
        String hashPassword = BCrypt.hashpw(plainPassword, salt);
        User user = new User(name, hashPassword, role);
        return this.users.create(user);
    }

    public Optional<User> passwordHashMatch(String name, String plainPassword) {
        Optional<User> user = this.users.getByName(name);
        if (user.isEmpty()) {
            return Optional.empty();
        }
        String hash = user.get().getPasswordHash();
        boolean valid = BCrypt.checkpw(plainPassword, hash);
        if (!valid) {
            return Optional.empty();
        }
        return Optional.of(user.get());
    }
}
