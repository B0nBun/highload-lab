package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.repository;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u where u.id = :id")
    Optional<User> findById(@Param("id") Long id);

    @Query("select u from User u where u.name = :name")
    Optional<User> findByName(@Param("name") String name);

    @Transactional
    @Modifying
    @Query("update User u set u.passwordHash = :passwordHash where u.id = :id")
    int updatePasswordHash(@Param("id") Long id, @Param("passwordHash") String passwordHash);

    @Transactional
    @Modifying
    @Query("delete User u where u.id = :id")
    int deleteByIdReturning(@Param("id") Long id);
}
