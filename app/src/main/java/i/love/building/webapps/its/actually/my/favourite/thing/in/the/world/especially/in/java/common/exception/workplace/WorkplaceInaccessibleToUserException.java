package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.workplace;

public class WorkplaceInaccessibleToUserException extends Exception {
    private Long userId;
    private Long workplaceId;

    public WorkplaceInaccessibleToUserException(Long userId, Long workplaceId) {
        this.userId = userId;
        this.workplaceId = workplaceId;
    }

    public Long getUserId() {
        return this.userId;
    }

    public Long getWorkplaceId() {
        return this.workplaceId;
    }
}
