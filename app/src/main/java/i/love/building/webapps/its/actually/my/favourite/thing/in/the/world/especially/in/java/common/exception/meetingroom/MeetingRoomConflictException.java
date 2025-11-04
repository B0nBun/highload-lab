package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.meetingroom;

import java.time.Instant;

public class MeetingRoomConflictException extends Exception {
    private Long meetingRoomId;
    private Instant startTime;
    private Instant endTime;

    public MeetingRoomConflictException(Long meetingRoomId, Instant startTime, Instant endTime) {
        this.meetingRoomId = meetingRoomId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getMeetingRoomId() {
        return this.meetingRoomId;
    }

    public Instant getStartTime() {
        return this.startTime;
    }

    public Instant getEndTime() {
        return this.endTime;
    }
}
