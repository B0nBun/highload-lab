package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model;  

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="name", unique = true, length = 255, nullable = false)
    private String name;

    @Column(name="password_hash", length = 60, nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name="role", nullable = false)
    private User.Role role;

    public static enum Role {
        admin,
        supervisor,
        regular,
        infrequent;
    }

    public User() {}

    public User(String name, String passwordHash, User.Role role) {
        this.name = name;
        this.passwordHash = passwordHash;
        this.role = role;
    }
    
    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getPasswordHash() {
        return this.passwordHash;
    }

    public User.Role getRole() {
        return this.role;
    }

    public String toString() {
        return String.format("User[id=%d, name='%s', phash='%s', role='%s']", this.id, this.name, this.passwordHash, this.role);
    }
}
