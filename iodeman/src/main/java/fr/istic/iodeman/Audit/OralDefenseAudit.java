package fr.istic.iodeman.Audit;

import fr.istic.iodeman.model.Participant;
import fr.istic.iodeman.model.Person;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table
public class OralDefenseAudit {

    @Id
    @GeneratedValue
    private Integer id;

    @OneToOne
    private Participant composition;

    @ManyToOne
    private RoomAudit roomAudit;

    @Embedded
    @AttributeOverrides( {
            @AttributeOverride(name = "from", column = @Column(name = "period_from")),
            @AttributeOverride(name = "to", column = @Column(name = "period_to"))
    })
    private TimeBoxAudit timeboxAudit;

    @OneToMany
    private Collection<Person> jury;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public Participant getComposition() {
        return composition;
    }
    public void setComposition(Participant composition) {
        this.composition = composition;
    }
    public RoomAudit getRoomAudit() {
        return roomAudit;
    }
    public void setRoomAudit(RoomAudit roomAudit) {
        this.roomAudit = roomAudit;
    }
    public TimeBoxAudit getTimeboxAudit() {
        return timeboxAudit;
    }
    public void setTimeboxAudit(TimeBoxAudit timeboxAudit) {
        this.timeboxAudit = timeboxAudit;
    }
    public Collection<Person> getJury() {
        return jury;
    }
    public void setJury(Collection<Person> jury) {
        this.jury = jury;
    }
}
