package fr.istic.iodeman.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Room implements Comparable<Room>{
	@Id
	@GeneratedValue
	private Integer id;
	
	private String name;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	
	public int compareTo(Room o) {
		return getName().compareTo(o.getName());
	}
}
