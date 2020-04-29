package fr.istic.iodeman.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.util.Collection;


@Entity
public class OralDefense extends AuditModel {
	
	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	private Person student;
	@ManyToOne
	private Person followingTeacher;

	private String tutorFullName;

	private String company;

	
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

	@Transient
	private Collection<TimeBox> unavailabilities;

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

	public Collection<TimeBox> getUnavailabilities() {
		return unavailabilities;
	}

	public void setUnavailabilities(Collection<TimeBox> unavailabilities) {
		this.unavailabilities = unavailabilities;
	}

	public Person getStudent() {
		return student;
	}

	public void setStudent(Person student) {
		this.student = student;
	}

	public Person getFollowingTeacher() {
		return followingTeacher;
	}

	public void setFollowingTeacher(Person followingTeacher) {
		this.followingTeacher = followingTeacher;
	}

	public String getTutorFullName() {
		return tutorFullName;
	}

	public void setTutorFullName(String tutorFullName) {
		this.tutorFullName = tutorFullName;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}


	@Override
	public String toString() {
		return  "Etudiant : "  + student.getFirstName() + " " + student.getLastName() + ", " +
				"Enseignant référent : " + followingTeacher.getFirstName() + " " + followingTeacher.getLastName() + ", " +
				"Entreprise : " + tutorFullName + " / " + company;
	}


}
