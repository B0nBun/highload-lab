package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.repository;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.Group;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface GroupRepository extends JpaRepository<Group, Long> {
  @Query("select g from Group g")
  List<Group> findAllWithoutDetails();

  @Query("select g from Group g where g.id = :id")
  Optional<Group> findById(@Param("id") Long id);

  @Query("select g from Group g where g.name = :name")
  Optional<Group> findByName(@Param("name") String name);

  @Transactional
  @Modifying
  @Query("delete Group g where g.id = :id")
  int deleteByIdReturning(@Param("id") Long id);
}
