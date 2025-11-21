package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.repository;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.MeetingRoom;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface MeetingRoomRepository extends JpaRepository<MeetingRoom, Long> {
    @Query("select mr from MeetingRoom mr where mr.office.id = :office_id")
    List<MeetingRoom> findByOfficeId(@Param("office_id") Long officeId);

    @Transactional
    @Modifying
    @Query("delete MeetingRoom mr where mr.id = :id")
    int deleteByIdReturning(@Param("id") Long id);
}
