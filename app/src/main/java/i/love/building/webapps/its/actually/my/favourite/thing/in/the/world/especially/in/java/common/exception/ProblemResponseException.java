package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class ProblemResponseException extends ErrorResponseException {
  public ProblemResponseException(HttpStatus status, String format, Object... args) {
    super(status, ProblemDetail.forStatusAndDetail(status, String.format(format, args)), null);
  }

  public ProblemResponseException(
      HttpStatus status, Throwable cause, String format, Object... args) {
    super(status, ProblemDetail.forStatusAndDetail(status, String.format(format, args)), cause);
  }
}
