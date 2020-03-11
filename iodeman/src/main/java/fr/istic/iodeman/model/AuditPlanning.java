package fr.istic.iodeman.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Collection;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


public class AuditPlanning {
    @Id
    @GeneratedValue
    private Integer id;

   //private Integer idPlaning;

    private Integer etat;

    private String name;

    private String version;
    private TimeBox timeBox;
    private String csv_file;

   /* @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Planning planning;*/

    @Embedded
    @AttributeOverrides( {
            @AttributeOverride(name = "from", column = @Column(name = "period_from")),
            @AttributeOverride(name = "to", column = @Column(name = "period_to"))
    })
    private TimeBox period;

    private Integer oralDefenseDuration;
    private Integer oralDefenseInterlude;
    @Embedded
    @AttributeOverrides( {
            @AttributeOverride(name = "from", column = @Column(name = "lunchBreak_from")),
            @AttributeOverride(name = "to", column = @Column(name = "lunchBreak_to"))
    })
    private TimeBox lunchBreak;
    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "from", column = @Column(name = "dayPeriod_from")),
            @AttributeOverride(name = "to", column = @Column(name = "dayPeriod_to"))
    })
    private TimeBox dayPeriod;
    private Integer nbMaxOralDefensePerDay;
    private Integer is_ref;
    private Integer ref_id;
    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany
    private Collection<Room> rooms;
    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany
    private Collection<Participant> participants;
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = CascadeType.PERSIST)
    private Collection<Priority> priorities;
    @JsonIgnore
    @OneToMany
    private Collection<OralDefense> oralDefenses;
    @ManyToOne
    private Person admin;
    @ManyToOne
    private Planning planning;

    public AuditPlanning(){}

    public AuditPlanning(AuditPlanning Ap){
        this.id = Ap.getId();
        this.name = Ap.getName();
        this.etat = Ap.getEtat();
        this.csv_file = Ap.getCsv_file();
        this.period = Ap.getPeriod();
        this.oralDefenseDuration = Ap.getOralDefenseDuration();
        this.oralDefenseInterlude = Ap.getOralDefenseInterlude();
        this.lunchBreak = Ap.getLunchBreak();
        this.dayPeriod = Ap.getDayPeriod();
        this.nbMaxOralDefensePerDay = Ap.getNbMaxOralDefensePerDay();
        this.is_ref = Ap.getIs_ref();
        this.ref_id = Ap.getRef_id();
        this.rooms = Ap.getRoomsAudit();
        this.participants = Ap.getParticipants();
        this.priorities = Ap.getPriorities();
        this.oralDefenses = Ap.getOralDefenses();
        this.admin = Ap.getAdmin();
    }
    public Integer getId() {
        return id;
    }
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    public TimeBox getTimeBox() {
        return timeBox;
    }
    public void setTimeBox(TimeBox timeBox) {
        this.timeBox = timeBox;
    }
    public Planning getPlanning() {
        return planning;
    }
    public void setPlanning(Planning planning) {
        this.planning = planning;
    }

    public Integer getEtat() {
        return etat;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public TimeBox getPeriod() {
        return period;
    }
    public void setPeriod(TimeBox period) {
        this.period = period;
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
    public Integer getNbMaxOralDefensePerDay() {
        return nbMaxOralDefensePerDay;
    }
    public void setNbMaxOralDefensePerDay(Integer nbMaxOralDefensePerDay) {
        this.nbMaxOralDefensePerDay = nbMaxOralDefensePerDay;
    }
    public void setIs_ref(Integer is_ref) { this.is_ref = is_ref; }
    public void setRef_id(Integer ref_id) {	this.ref_id = ref_id;}
    public Integer getIs_ref() { return is_ref;	}
    public Integer getRef_id() { return ref_id; }
    public Collection<Room> getRoomsAudit() {
        return rooms;
    }
    public void setRooms(Collection<Room> rooms) {
        this.rooms = rooms;
    }
    public Collection<Participant> getParticipants() {
        return participants;
    }
    public void setParticipants(Collection<Participant> participants) {
        this.participants = participants;
    }
    public Collection<Priority> getPriorities() {
        return priorities;
    }
    public void setPriorities(Collection<Priority> priorities) {
        this.priorities = priorities;
    }
    public Collection<OralDefense> getOralDefenses() {
        return oralDefenses;
    }
    public void setOralDefenses(Collection<OralDefense> oralDefenses) {
        this.oralDefenses = oralDefenses;
    }
    public void setEtat(Integer etat) {
        this.etat = etat;
    }
    public Person getAdmin() {
        return admin;
    }
    public void setAdmin(Person admin) {
        this.admin = admin;
    }
    public String getCsv_file() { return csv_file;	}
    public void setCsv_file(String csv_file) {this.csv_file = csv_file;	}
}
