package fr.istic.iodeman.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;


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
	private TimeBox timeBox;

	@OneToOne
	private Person secondTeacher;

	@ManyToOne
	@JsonIgnore
	private Planning planning;

	private Integer number;

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

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
	public TimeBox getTimeBox() {
		return timeBox;
	}
	public void setTimeBox(TimeBox timeBox) {
		this.timeBox = timeBox;
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
