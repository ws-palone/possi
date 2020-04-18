package fr.istic.iodeman.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.envers.Audited;

import javax.persistence.*;


@Entity
@Audited
public class Unavailability extends AuditModel {

	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="person_id")
	private Person person;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name="planning_id")
	private Planning planning;

	@Embedded
	@AttributeOverrides( {
		@AttributeOverride(name = "from", column = @Column(name = "period_from")),
		@AttributeOverride(name = "to", column = @Column(name = "period_to"))
	})
	private TimeBox period;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Person getPerson() {
		return person;
	}
	
	public void setPerson(Person person) {
		this.person = person;
	}
	
	public TimeBox getPeriod() {
		return period;
	}
	
	public void setPeriod(TimeBox period) {
		this.period = period;
	}
	
	public Planning getPlanning() {
		return planning;
	}

	public void setPlanning(Planning planning) {
		this.planning = planning;
	}

}