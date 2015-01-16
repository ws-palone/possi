package fr.istic.iodeman.model;

public class Room implements Comparable<Room>{
	private int id;
	private String name;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	@Override
	public int compareTo(Room o) {
		return getName().compareTo(o.getName());
	}
	
	
}
