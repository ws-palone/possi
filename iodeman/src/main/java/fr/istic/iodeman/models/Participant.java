package fr.istic.iodeman.models;

import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


@Entity
@Audited
public class Participant extends AuditModel {
	
	@Id
	@GeneratedValue
	private Long id;
	@ManyToOne
	private Person student;
	@ManyToOne
	private Person followingTeacher;
	
	private String tutorFullName;
	
	private String company;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
