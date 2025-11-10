package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "workplace_booking")
public class WorkplaceBooking {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "workplace_id")
  private Workplace workplace;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "booked_date")
  private LocalDate bookedDate;

  public WorkplaceBooking() {}

  public WorkplaceBooking(Workplace workpace, User user, LocalDate bookedDate) {
    this.workplace = workpace;
    this.user = user;
    this.bookedDate = bookedDate;
  }

  public Long getId() {
    return this.id;
  }

  public Workplace getWorkplace() {
    return this.workplace;
  }

  public User getUser() {
    return this.user;
  }

  public LocalDate getBookedDate() {
    return this.bookedDate;
  }
}
