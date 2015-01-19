package fr.istic.iodeman.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table
public class Unavailability {
	@Id
	@GeneratedValue
	@Column
	private int id;
	@Column
	private Person person;
	@Column
	private TimeBox period;
	
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
	
}
