package fr.istic.iodeman.model.revision;

import fr.istic.iodeman.model.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class PlanningRevision {
	
	@Id
	@GeneratedValue
	private Long id;
	private String name;
	private Integer oralDefenseDuration;
	private Integer oralDefenseInterlude;
	private Integer nbMaxOralDefensePerDay;
	private Integer is_ref;
	private Integer ref_id;
	private Planning planning;
	private String revision;

	@ManyToOne
	private PersonRevision admin;

	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany
	private Collection<RoomRevision> rooms;

	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany
	private Collection<ParticipantRevision> participants;

	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(cascade = CascadeType.PERSIST)
	private Collection<PriorityRevision> priorities;

	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy = "planning", cascade = CascadeType.PERSIST)
	private Collection<OralDefenseRevision> oralDefenses;


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

	public PlanningRevision(){}

	public PlanningRevision(Planning p){
		this.name = p.getName();
		this.period = p.getPeriod();
		this.oralDefenseDuration = p.getOralDefenseDuration();
		this.oralDefenseInterlude = p.getOralDefenseInterlude();
		this.lunchBreak = p.getLunchBreak();
		this.dayPeriod = p.getDayPeriod();
		this.nbMaxOralDefensePerDay = p.getNbMaxOralDefensePerDay();
		this.is_ref = p.getIs_ref();
		this.ref_id = p.getRef_id();
		this.planning = p;
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

	public Collection<RoomRevision> getRooms() {
		return rooms;
	}

	public void setRooms(Collection<RoomRevision> rooms) {
		this.rooms = rooms;
	}

	public Collection<ParticipantRevision> getParticipants() {
		return participants;
	}

	public void setParticipants(Collection<ParticipantRevision> participants) {
		this.participants = participants;
	}

	public Collection<PriorityRevision> getPriorities() {
		return priorities;
	}

	public void setPriorities(Collection<PriorityRevision> priorities) {
		this.priorities = priorities;
	}

	public Collection<OralDefenseRevision> getOralDefenses() {
		return oralDefenses;
	}

	public void setOralDefenses(Collection<OralDefenseRevision> oralDefenses) {
		this.oralDefenses = oralDefenses;
	}

	public PersonRevision getAdmin() {
		return admin;
	}

	public void setAdmin(PersonRevision admin) {
		this.admin = admin;
	}

	public Planning getPlanning() {
		return planning;
	}

	public void setPlanning(Planning planning) {
		this.planning = planning;
	}
}
