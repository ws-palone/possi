package fr.istic.iodeman.models.revision;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.istic.iodeman.models.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class PlanningRevision extends AuditModel {

    @Id
    @GeneratedValue
    private Long id;
    private Integer oralDefenseDuration;
    private Integer oralDefenseInterlude;
    private String name;
    private Integer nbMaxOralDefensePerDay;
    @Embedded
    @AttributeOverrides( {
            @AttributeOverride(name = "from", column = @Column(name = "period_from")),
            @AttributeOverride(name = "to", column = @Column(name = "period_to"))
    })
    private TimeBox period;

    @Embedded
    @AttributeOverrides( {
            @AttributeOverride(name = "from", column = @Column(name = "lunchBreak_from")),
            @AttributeOverride(name = "to", column = @Column(name = "lunchBreak_to"))
    })
    private TimeBox lunchBreak;

    @Embedded
    @AttributeOverrides( {
            @AttributeOverride(name = "from", column = @Column(name = "dayPeriod_from")),
            @AttributeOverride(name = "to", column = @Column(name = "dayPeriod_to"))
    })
    private TimeBox dayPeriod;

    @ManyToOne
    @JsonIgnore
    private Planning planning;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany
    private Collection<RoomRevision> rooms;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(cascade = CascadeType.PERSIST)
    private Collection<Priority> priorities;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "planning", cascade = CascadeType.REMOVE)
    private Collection<OralDefenseRevision> oralDefenses;

    @ManyToOne
    private Person admin;

    @OneToOne
    private RevInfo revInfo;

    public PlanningRevision() {}

    public PlanningRevision(Planning p) {
        this.admin = p.getAdmin();
        this.name = p.getName();
        this.period = p.getPeriod();
        this.oralDefenseDuration = p.getOralDefenseDuration();
        this.oralDefenseInterlude = p.getOralDefenseInterlude();
        this.lunchBreak = p.getLunchBreak();
        this.dayPeriod = p.getDayPeriod();
        this.nbMaxOralDefensePerDay = p.getNbMaxOralDefensePerDay();
        this.planning = p;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOralDefenseDuration() {
        return oralDefenseDuration;
    }

    public void setOralDefenseDuration(Integer oralDefenseDuration) {
        this.oralDefenseDuration = oralDefenseDuration;
    }

    public Integer getOralDefenseInterlude() {
        return oralDefenseInterlude;
    }

    public void setOralDefenseInterlude(Integer oralDefenseInterlude) {
        this.oralDefenseInterlude = oralDefenseInterlude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNbMaxOralDefensePerDay() {
        return nbMaxOralDefensePerDay;
    }

    public void setNbMaxOralDefensePerDay(Integer nbMaxOralDefensePerDay) {
        this.nbMaxOralDefensePerDay = nbMaxOralDefensePerDay;
    }

    public TimeBox getPeriod() {
        return period;
    }

    public void setPeriod(TimeBox period) {
        this.period = period;
    }

    public TimeBox getLunchBreak() {
        return lunchBreak;
    }

    public void setLunchBreak(TimeBox lunchBreak) {
        this.lunchBreak = lunchBreak;
    }

    public TimeBox getDayPeriod() {
        return dayPeriod;
    }

    public void setDayPeriod(TimeBox dayPeriod) {
        this.dayPeriod = dayPeriod;
    }

    public Collection<RoomRevision> getRooms() {
        return rooms;
    }

    public void setRooms(Collection<RoomRevision> rooms) {
        this.rooms = rooms;
    }

    public Collection<Priority> getPriorities() {
        return priorities;
    }

    public void setPriorities(Collection<Priority> priorities) {
        this.priorities = priorities;
    }

    public Collection<OralDefenseRevision> getOralDefenses() {
        return oralDefenses;
    }

    public void setOralDefenses(Collection<OralDefenseRevision> oralDefenses) {
        this.oralDefenses = oralDefenses;
    }

    public Person getAdmin() {
        return admin;
    }

    public void setAdmin(Person admin) {
        this.admin = admin;
    }

    public RevInfo getRevInfo() {
        return revInfo;
    }

    public void setRevInfo(RevInfo revInfo) {
        this.revInfo = revInfo;
    }

    public Planning getPlanning() {
        return planning;
    }

    public void setPlanning(Planning planning) {
        this.planning = planning;
    }
}
