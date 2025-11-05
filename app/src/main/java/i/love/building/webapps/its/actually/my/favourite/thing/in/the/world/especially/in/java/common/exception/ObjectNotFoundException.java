package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception;

import org.springframework.http.HttpStatus;

public class NotFoundResponseException extends Exception {
    String object;
    
    public NotFoundResponseException(String object) {
        this.object = object;
    }

    public NotFoundResponseException(String fmt, Object... args) {
        this.object = String.format(fmt, args);
    }

    public ProblemResponseException responseException() {
        return new ProblemResponseException(HttpStatus.NOT_FOUND, "%s is not found", this.object);
    }
}
