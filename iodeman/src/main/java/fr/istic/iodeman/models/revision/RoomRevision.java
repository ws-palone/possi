package fr.istic.iodeman.models.revision;

import fr.istic.iodeman.models.AuditModel;
import fr.istic.iodeman.models.Room;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class RoomRevision extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    public RoomRevision() {}

    public RoomRevision(Room room) {
        this.name = room.getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
