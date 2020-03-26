package fr.istic.iodeman.model;

import javax.persistence.*;
import java.util.Collection;


@Entity
@Table
public class OralDefense {
	
	@Id
	@GeneratedValue
	private Integer id;
	
	@OneToOne
	private Participant composition;
	
	@ManyToOne
	private Room room;
	
	@Embedded
	@AttributeOverrides( {
		@AttributeOverride(name = "from", column = @Column(name = "period_from")),
		@AttributeOverride(name = "to", column = @Column(name = "period_to"))
	})
	private TimeBox timebox;

	@OneToOne
	private Person secondTeacher;

	@ManyToOne
	private Planning planning;
	
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
	public Room getRoom() {
		return room;
	}
	public void setRoom(Room room) {
		this.room = room;
	}
	public TimeBox getTimebox() {
		return timebox;
	}
	public void setTimebox(TimeBox timebox) {
		this.timebox = timebox;
	}
	public Person getSecondTeacher() {
		return secondTeacher;
	}
	public void setSecondTeacher(Person secondTeacher) {
		this.secondTeacher = secondTeacher;
	}

	public Planning getPlanning() {
		return planning;
	}

	public void setPlanning(Planning planning) {
		this.planning = planning;
	}
}
