package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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
        return String.format("Office[id=%d, name='%s', map=(hash:%s)]", this.id, this.name, this.map.hashCode());
    }
}
