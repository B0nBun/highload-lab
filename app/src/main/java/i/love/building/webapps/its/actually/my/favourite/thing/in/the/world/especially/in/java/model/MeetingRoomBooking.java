package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "meeting_booking")
public class MeetingRoomBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "meeting_room_id")
    private MeetingRoom meetingRoom;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "start_time")
    private Timestamp startTime;

    @Column(name = "end_time")
    private Timestamp endTime;

    public MeetingRoomBooking() {}

    public MeetingRoomBooking(
            MeetingRoom meetingRoom, User user, Timestamp startTime, Timestamp endTime) {
        this.meetingRoom = meetingRoom;
        this.user = user;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getId() {
        return this.id;
    }

    public MeetingRoom getMeetingRoom() {
        return this.meetingRoom;
    }

    public User getUser() {
        return this.user;
    }

    public Timestamp getStartTime() {
        return this.startTime;
    }

    public Timestamp getEndTime() {
        return this.endTime;
    }
}
