package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.meetingroom;

public class MeetingRoomInaccessibleToUserException extends Exception {
    private Long userId;
    private Long meetingRoomId;

    public MeetingRoomInaccessibleToUserException(Long userId, Long meetingRoomId) {
        this.userId = userId;
        this.meetingRoomId = meetingRoomId;
    }

    public Long getUserId() {
        return this.userId;
    }

    public Long getMeetingRoomId() {
        return this.meetingRoomId;
    }
}
