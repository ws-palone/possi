package fr.istic.iodeman.models;

import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Audited
public class Person extends AuditModel implements Comparable<Person> {
	@Id
	@GeneratedValue
	private Long id;
	private String firstName;
	private String lastName;
	private Role role;
	private String uid;
	private String email;

	public String getUid() {
		return uid;
	}
	
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public int compareTo(Person p) {
		return this.getUid().compareTo(p.getUid());
	}

	public boolean equals(Person p) {
		return this.uid.equals(p.getUid());
	}
}
