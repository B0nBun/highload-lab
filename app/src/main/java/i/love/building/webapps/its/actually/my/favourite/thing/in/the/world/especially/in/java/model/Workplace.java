package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "workplace")
public class Workplace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "monitors", nullable = false)
    private Long monitors;

    @Column(name = "audio_equipment", nullable = false)
    @Enumerated(EnumType.STRING)
    private AudioEquipmentState audioEquipment;

    @JoinColumn(name = "office_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Office office;

    public static enum AudioEquipmentState {
        absent,
        headset,
        speakers;
    }

    public Workplace() {}

    public Workplace(Office office, Long monitors, AudioEquipmentState audioEquipment) {
        this.monitors = monitors;
        this.audioEquipment = audioEquipment;
        this.office = office;
    }

    public Workplace(Long id, Office office, Long monitors, AudioEquipmentState audioEquipmentState) {
        this.id = id;
        this.office = office;
        this.monitors = monitors;
        this.audioEquipment = audioEquipmentState;
    }

    public Long getId() {
        return this.id;
    }

    public Long getMonitors() {
        return this.monitors;
    }

    public AudioEquipmentState getAudioEquipment() {
        return this.audioEquipment;
    }

    public Office getOffice() {
        return this.office;
    }
}
