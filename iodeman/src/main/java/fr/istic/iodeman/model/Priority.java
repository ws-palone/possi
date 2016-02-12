package fr.istic.iodeman.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Priority {
	@Id
	@GeneratedValue
	private Integer id;	

	private String role;

	private Integer weight;
	
	public Priority() {
		
	}
	
	public Priority(String role, Integer weight) {
		this.role = role;
		setWeight(weight);
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
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
