package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "groups")
public class Group {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  Long id;

  @Column(name = "name", unique = true, length = 255, nullable = false)
  String name;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "groups_office_rel",
      joinColumns = @JoinColumn(name = "groups_id"),
      inverseJoinColumns = @JoinColumn(name = "office_id"))
  Set<Office> offices = new HashSet<>();

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "user_groups_rel",
      joinColumns = @JoinColumn(name = "groups_id"),
      inverseJoinColumns = @JoinColumn(name = "user_id"))
  Set<User> users = new HashSet<>();

  public Group() {}

  public Group(String name) {
    this.name = name;
  }

  public Long getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public Set<User> getUsers() {
    return new HashSet<>(this.users);
  }

  public Set<Office> getOffices() {
    return new HashSet<>(this.offices);
  }

  public void addUser(User user) {
    this.users.add(user);
  }

  public void addOffice(Office office) {
    this.offices.add(office);
  }

  public void removeUser(Long userId) {
    this.users.removeIf(u -> u.getId() == userId);
  }

  public void removeOffice(Long officeId) {
    this.offices.removeIf(o -> o.getId() == officeId);
  }
}
