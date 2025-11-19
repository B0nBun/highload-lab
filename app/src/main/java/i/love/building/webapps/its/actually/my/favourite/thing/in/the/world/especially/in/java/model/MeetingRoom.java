package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "meeting_room")
public class MeetingRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "office_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Office office;

    @Column(name = "remote_available", nullable = false)
    private boolean remoteAvaialable;

    @Column(name = "capacity", nullable = false)
    private Long capacity;

    public MeetingRoom() {}

    public MeetingRoom(Office office, boolean remoteAvaialable, Long capacity) {
        this.office = office;
        this.remoteAvaialable = remoteAvaialable;
        this.capacity = capacity;
    }

    public MeetingRoom(Long id, Office office, boolean remoteAvaialable, Long capacity) {
        this.id = id;
        this.office = office;
        this.remoteAvaialable = remoteAvaialable;
        this.capacity = capacity;
    }

    public Long getId() {
        return this.id;
    }

    public Office getOffice() {
        return this.office;
    }

    public boolean getRemoteAvailable() {
        return this.remoteAvaialable;
    }

    public Long getCapacity() {
        return this.capacity;
    }
}
