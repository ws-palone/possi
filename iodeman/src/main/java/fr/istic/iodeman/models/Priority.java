package fr.istic.iodeman.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Priority extends AuditModel {
	@Id
	@GeneratedValue
	private Long id;

	private String role;

	private Integer weight;
	
	public Priority() {}
	
	public Priority(String role, Integer weight) {
		this.role = role;
		setWeight(weight);
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}
}
