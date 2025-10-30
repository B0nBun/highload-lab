package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.Workplace;

public interface WorkplaceRepository extends JpaRepository<Workplace, Long> {
    @Query("select w from Workplace w where w.office.id = :office_id")
    List<Workplace> findByOfficeId(@Param("office_id") Long officeId);   

    @Transactional
    @Modifying
    @Query("delete Workplace w where w.id = :id")
    int deleteByIdReturning(@Param("id") Long id);
}
