package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.WorkplaceBooking;

public interface WorkplaceBookingRepository extends JpaRepository<WorkplaceBooking, Long> {
    @Query("select b from WorkplaceBooking b where b.workplace.id = :workplace_id")
    List<WorkplaceBooking> findByWorkplaceId(@Param("workplace_id") Long workplaceId);

    @Query("select b from WorkplaceBooking b where b.user.id = :user_id")
    List<WorkplaceBooking> findByUserId(@Param("user_id") Long userId);

    @Query("select b from WorkplaceBooking b where b.user.id = :user_id and b.bookedDate = :booked_date")
    Optional<WorkplaceBooking> findByUserIdAndDate(@Param("user_id") Long userId, @Param("booked_date") LocalDate date);

    @Query("select b from WorkplaceBooking b where b.workplace.id = :workplace_id and b.bookedDate = :booked_date")
    Optional<WorkplaceBooking> findByWorkplaceAndDate(@Param("workplace_id") Long workplaceId, @Param("booked_date") LocalDate date);

    @Transactional
    @Modifying
    @Query("delete WorkplaceBooking b where b.id = :id")
    int deleteByIdReturning(@Param("id") Long id);
}
