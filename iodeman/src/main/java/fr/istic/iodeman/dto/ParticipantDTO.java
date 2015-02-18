package fr.istic.iodeman.dto;

public class ParticipantDTO {

	private PersonDTO student;
	private PersonDTO followingTeacher;
	
	public ParticipantDTO(){
		
	}
	
	public PersonDTO getStudent() {
		return student;
	}
	public void setStudent(PersonDTO student) {
		this.student = student;
	}
	public PersonDTO getFollowingTeacher() {
		return followingTeacher;
	}
	public void setFollowingTeacher(PersonDTO followingTeacher) {
		this.followingTeacher = followingTeacher;
	}
}
