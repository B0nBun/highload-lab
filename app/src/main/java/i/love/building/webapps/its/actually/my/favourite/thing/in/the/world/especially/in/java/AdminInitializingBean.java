package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.AlreadyExistsException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.UnreachableCodeException;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.User;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.repository.UserRepository;
import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.service.AuthService;
import java.util.Optional;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializingBean implements InitializingBean {
    @Value("${lab.adminPassword}")
    private String adminPassword;

    @Value("${lab.adminUsername}")
    private String adminUsername;

    @Autowired private UserRepository usersRepo;
    @Autowired private AuthService auth;

    @Override
    public void afterPropertiesSet() {
        Optional<Long> adminId = this.usersRepo.findByName(this.adminUsername).map(User::getId);
        if (adminId.isEmpty()) {
            try {
                this.auth.registerUser(this.adminUsername, this.adminPassword, User.Role.admin);
            } catch (AlreadyExistsException e) {
                throw new UnreachableCodeException();
            }
            return;
        }
        this.auth.changePassword(adminId.get(), adminPassword);
    }
}
