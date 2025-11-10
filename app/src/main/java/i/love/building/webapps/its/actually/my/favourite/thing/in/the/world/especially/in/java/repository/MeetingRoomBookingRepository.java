package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.repository;

import i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model.MeetingRoomBooking;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface MeetingRoomBookingRepository extends JpaRepository<MeetingRoomBooking, Long> {
    @Query("select b from MeetingRoomBooking b where b.meetingRoom.id = :meeting_room_id")
    List<MeetingRoomBooking> findByMeetingRoomId(@Param("meeting_room_id") Long meetingRoomId);

    @Query("select b from MeetingRoomBooking b where b.user.id = :user_id")
    List<MeetingRoomBooking> findByUserId(@Param("user_id") Long userId);

    @Query(
            "select b from MeetingRoomBooking b where b.meetingRoom.id = :room_id and b.startTime"
                    + " <= :end and b.endTime >= :start")
    Optional<MeetingRoomBooking> findByRoomAndOverlap(
            @Param("room_id") Long meetingRoomId,
            @Param("start") Timestamp start,
            @Param("end") Timestamp end);

    @Transactional
    @Modifying
    @Query("delete MeetingRoomBooking b where b.id = :id")
    int deleteByIdReturning(@Param("id") Long id);
}
