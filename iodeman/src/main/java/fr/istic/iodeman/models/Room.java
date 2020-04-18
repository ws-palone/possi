package fr.istic.iodeman.models;

import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Audited
public class Room extends AuditModel implements Comparable<Room>{
	@Id
	@GeneratedValue
	private Long id;
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}
	
	public int compareTo(Room o) {
		return getName().compareTo(o.getName());
	}
}
