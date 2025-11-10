package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "office")
public class Office {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", unique = true, nullable = false, length = 255)
    private String name;

    @Column(name = "map", nullable = true)
    private byte[] map;

    @ManyToMany(mappedBy = "offices")
    private List<Group> groups = new ArrayList<>();

    public Office() {}

    public Office(String name, byte[] map) {
        this.name = name;
        this.map = map;
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public byte[] getMap() {
        return this.map;
    }

    public String toString() {
        return String.format(
                "Office[id=%d, name='%s', map=(hash:%s)]", this.id, this.name, this.map.hashCode());
    }
}
