package fr.istic.iodeman.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table
public class Participant implements Serializable{
	
	@Id
	@GeneratedValue
	@Column
	private Integer id;
	@Column
	private Person student;
	@Column
	private Person followingTeacher;
	@Column
	private Planning planning;
	
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
}
