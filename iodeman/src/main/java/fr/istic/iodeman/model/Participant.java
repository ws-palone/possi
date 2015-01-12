package fr.istic.iodeman.model;

public class Participant {
	
	private Person student;
	private Person followingTeacher;
	
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
