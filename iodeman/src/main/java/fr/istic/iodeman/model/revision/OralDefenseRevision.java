package fr.istic.iodeman.model.revision;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.istic.iodeman.model.*;

import javax.persistence.*;


@Entity
@Table
public class OralDefenseRevision {
	
	@Id
	@GeneratedValue
	private Integer id;
	@OneToOne
	private ParticipantRevision composition;
	@ManyToOne
	private RoomRevision room;
	@ManyToOne
	@JsonIgnore
	private PlanningRevision planning;
	private Integer number;
	@ManyToOne
	private Color color;
	@OneToOne
	private PersonRevision secondTeacher;

	public OralDefenseRevision() {}

	public OralDefenseRevision(OralDefense oralDefense) {
		this.number = oralDefense.getNumber();
		this.color = oralDefense.getColor();
	}
	
	@Embedded
	@AttributeOverrides( {
		@AttributeOverride(name = "from", column = @Column(name = "period_from")),
		@AttributeOverride(name = "to", column = @Column(name = "period_to"))
	})
	private TimeBox timeBox;

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
	
	public ParticipantRevision getComposition() {
		return composition;
	}

	public void setComposition(ParticipantRevision composition) {
		this.composition = composition;
	}

	public RoomRevision getRoom() {
		return room;
	}

	public void setRoom(RoomRevision room) {
		this.room = room;
	}

	public TimeBox getTimeBox() {
		return timeBox;
	}

	public void setTimeBox(TimeBox timeBox) {
		this.timeBox = timeBox;
	}

	public PersonRevision getSecondTeacher() {
		return secondTeacher;
	}

	public void setSecondTeacher(PersonRevision secondTeacher) {
		this.secondTeacher = secondTeacher;
	}

	public PlanningRevision getPlanning() {
		return planning;
	}

	public void setPlanning(PlanningRevision planning) {
		this.planning = planning;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
