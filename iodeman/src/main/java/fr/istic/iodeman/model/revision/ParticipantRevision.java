package fr.istic.iodeman.model.revision;

import fr.istic.iodeman.model.Participant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


@Entity
public class ParticipantRevision {
	
	@Id
	@GeneratedValue
	private Integer id;
	@ManyToOne
	private PersonRevision student;
	@ManyToOne
	private PersonRevision followingTeacher;
	private String tutorFullName;
	private String company;

	public ParticipantRevision() {}

	public ParticipantRevision(Participant participant) {
		this.company = participant.getCompany();
		this.tutorFullName = participant.getTutorFullName();
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public PersonRevision getStudent() {
		return student;
	}
	
	public void setStudent(PersonRevision student) {
		this.student = student;
	}
	
	public PersonRevision getFollowingTeacher() {
		return followingTeacher;
	}
	
	public void setFollowingTeacher(PersonRevision followingTeacher) {
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
