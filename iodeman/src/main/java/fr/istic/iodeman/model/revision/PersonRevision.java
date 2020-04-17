package fr.istic.iodeman.model.revision;

import fr.istic.iodeman.model.Person;
import fr.istic.iodeman.model.Role;
import org.hibernate.annotations.Formula;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table
public class PersonRevision implements Comparable<PersonRevision> {
	@Id
	@GeneratedValue
	private Integer id;
	
	private String firstName;
	private String lastName;
	private Role role;
	private String uid;
	private String email;
	private String promo;

	public PersonRevision() {}

	public PersonRevision(Person person) {
		this.firstName = person.getFirstName();
		this.lastName = person.getLastName();
		this.role = person.getRole();
		this.uid = person.getUid();
		this.email = person.getEmail();
		this.promo = person.getPromo();
	}

	public String getUid() {
		return uid;
	}
	
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int compareTo(PersonRevision p) {
		return this.getUid().compareTo(p.getUid());
	}

	public String getPromo() {
		return promo;
	}

	public void setPromo(String promo) {
		this.promo = promo;
	}
	
	public boolean equals(PersonRevision p) {
		return this.uid.equals(p.getUid());
	}
}
