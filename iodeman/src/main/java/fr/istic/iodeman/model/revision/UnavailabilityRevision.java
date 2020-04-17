package fr.istic.iodeman.model.revision;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.istic.iodeman.model.TimeBox;
import fr.istic.iodeman.model.Unavailability;

import javax.persistence.*;


@Entity
public class UnavailabilityRevision {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;

	@ManyToOne
	@JoinColumn(name="person_id")
	private PersonRevision person;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name="planning_id")
	private PlanningRevision planning;

	@Embedded
	@AttributeOverrides( {
		@AttributeOverride(name = "from", column = @Column(name = "period_from")),
		@AttributeOverride(name = "to", column = @Column(name = "period_to"))
	})
	private TimeBox period;

	public UnavailabilityRevision() {}

	public UnavailabilityRevision(Unavailability unavailability) {
		this.period = unavailability.getPeriod();
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public PersonRevision getPerson() {
		return person;
	}
	
	public void setPerson(PersonRevision person) {
		this.person = person;
	}
	
	public TimeBox getPeriod() {
		return period;
	}
	
	public void setPeriod(TimeBox period) {
		this.period = period;
	}
	
	public PlanningRevision getPlanning() {
		return planning;
	}

	public void setPlanning(PlanningRevision planning) {
		this.planning = planning;
	}
	
}