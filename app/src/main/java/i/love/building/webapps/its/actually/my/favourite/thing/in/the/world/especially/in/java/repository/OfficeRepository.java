package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.repository;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.Office;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface OfficeRepository extends JpaRepository<Office, Long> {
    @Query("select o from Office o where o.id = :id")
    Optional<Office> findById(@Param("id") Long id);

    @Query("select o from Office o where o.name = :name")
    Optional<Office> findByName(@Param("name") String name);

    @Transactional
    @Modifying
    @Query("delete Office o where o.id = :id")
    int deleteByIdReturning(@Param("id") Long id);
}
