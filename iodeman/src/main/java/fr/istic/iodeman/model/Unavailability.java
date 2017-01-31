package fr.istic.iodeman.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class Unavailability {


	@Id
	@GeneratedValue

	private int id;
	
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

	private int from_ref = 1;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
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
	public int getFrom_ref() {
		return from_ref;
	}

	public void setFrom_ref(int from_ref) {
		this.from_ref = from_ref;
	}
	
}