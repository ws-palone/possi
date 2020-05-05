package fr.istic.iodeman.models;

import fr.istic.iodeman.models.revision.PlanningRevision;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.history.RevisionMetadata;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Planning extends AuditModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	private Integer oralDefenseDuration;
	private Integer oralDefenseInterlude;
	private String name;
	private Integer nbMaxOralDefensePerDay;
	private Boolean generated = false;
	private Boolean newUnavailabilities = true;

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

	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany
	private Collection<Room> rooms;

	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany(cascade = CascadeType.PERSIST)
	private Collection<Priority> priorities;

	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy = "planning", cascade = CascadeType.REMOVE)
	private Collection<OralDefense> oralDefenses;

	@OneToMany(mappedBy = "planning", cascade = CascadeType.REMOVE)
	private Collection<PlanningRevision> revisions;

	@ManyToOne
	private Person admin;

	@OneToOne
	private PlanningRevision defaultRevision;

	public Planning(){}

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

	public Collection<Room> getRooms() {
		return rooms;
	}

	public void setRooms(Collection<Room> rooms) {
		this.rooms = rooms;
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

	public Person getAdmin() {
		return admin;
	}

	public void setAdmin(Person admin) {
		this.admin = admin;
	}

	public Collection<PlanningRevision> getRevisions() {
		return revisions;
	}

	public void setRevisions(Collection<PlanningRevision> revisions) {
		this.revisions = revisions;
	}

	public PlanningRevision getDefaultRevision() {
		return defaultRevision;
	}

	public void setDefaultRevision(PlanningRevision defaultRevision) {
		this.defaultRevision = defaultRevision;
	}

	public Boolean getGenerated() {
		return generated;
	}

	public void setGenerated(Boolean generated) {
		this.generated = generated;
	}

	public Boolean getNewUnavailabilities() {
		return newUnavailabilities;
	}

	public void setNewUnavailabilities(Boolean newUnavailabilities) {
		this.newUnavailabilities = newUnavailabilities;
	}
}
