package fr.istic.iodeman.dto;

public class ParticipantDTO {

	private PersonDTO student;
	private PersonDTO followingTeacher;
	private String tutorFullName;
	private String company;
	
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
