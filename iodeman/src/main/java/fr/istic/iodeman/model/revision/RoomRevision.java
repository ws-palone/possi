package fr.istic.iodeman.model.revision;

import fr.istic.iodeman.model.Room;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class RoomRevision implements Comparable<RoomRevision>{
	@Id
	@GeneratedValue
	private Integer id;
	
	private String name;

	public RoomRevision() {}

	public RoomRevision(Room room) {
		this.name = room.getName();
	}
	
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
	
	public int compareTo(RoomRevision o) {
		return getName().compareTo(o.getName());
	}
}
