package fr.istic.iodeman.dto;

import fr.istic.iodeman.model.Person;

public class PersonDTO {
	private Person person;
	private Integer unavailabilitiesNumber;
	
	public PersonDTO(){
		
	}
	
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public Integer getUnavailabilitiesNumber() {
		return unavailabilitiesNumber;
	}
	public void setUnavailabilitiesNumber(Integer unavailabilitiesNumber) {
		this.unavailabilitiesNumber = unavailabilitiesNumber;
	}
}
