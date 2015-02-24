package fr.istic.iodeman.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


@Entity
public class Participant {
	
	@Id
	@GeneratedValue
	private Integer id;
	@ManyToOne
	private Person student;
	@ManyToOne
	private Person followingTeacher;
	
	private String tutorFullName;
	
	private String company;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
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
	
}
