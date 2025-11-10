package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.common.exception.workplace;

import java.time.LocalDate;

public class WorkplaceUserAlreadyBookedException extends Exception {
  private LocalDate bookedDate;
  private Long userId;
  private Long workplaceId;

  public WorkplaceUserAlreadyBookedException(LocalDate date, Long userId, Long workplaceId) {
    this.bookedDate = date;
    this.userId = userId;
    this.workplaceId = workplaceId;
  }

  public LocalDate getBookedDate() {
    return this.bookedDate;
  }

  public Long getUserId() {
    return this.userId;
  }

  public Long getWorkplaceId() {
    return this.workplaceId;
  }
}
