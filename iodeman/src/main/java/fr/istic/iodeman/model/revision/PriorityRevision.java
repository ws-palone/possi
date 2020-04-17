package fr.istic.iodeman.model.revision;

import fr.istic.iodeman.model.Priority;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class PriorityRevision {
	@Id
	@GeneratedValue
	private Integer id;	

	private String role;

	private Integer weight;
	
	public PriorityRevision() {}
	
	public PriorityRevision(Priority priority) {
		this.role = priority.getRole();
		this.weight = priority.getWeight();
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
