package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.user;

public class UserAlreadyExistsException extends Exception {
    private Long userId;
    private String username;

    public UserAlreadyExistsException(Long userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    @Override
    public String getMessage() {
        return String.format("user with name '%s' already exists", this.username);
    }

    public Long getUserId() {
        return this.userId;
    }

    public String getUsername() {
        return this.username;
    }
}
