package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception;

public class UnreachableCodeException extends RuntimeException {
  @Override
  public String getMessage() {
    return "unreachable";
  }
}
