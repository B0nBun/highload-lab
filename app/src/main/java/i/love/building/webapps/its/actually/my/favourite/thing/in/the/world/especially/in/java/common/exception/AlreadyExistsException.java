package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception;

public class AlreadyExistsException extends Exception {
  private String kind;
  private String identifier;

  public AlreadyExistsException(String kind, String identifier) {
    this.kind = kind;
    this.identifier = identifier;
  }

  @Override
  public String getMessage() {
    return String.format("%s '%s' already exists", this.kind, this.identifier);
  }

  public String getIdentifier() {
    return this.identifier;
  }
}
