package fr.istic.iodeman.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;


@Entity
@Audited
public class OralDefense extends AuditModel {
	
	@Id
	@GeneratedValue
	private Long id;
	
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

	@ManyToOne
	@NotAudited
	private Color color;

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
