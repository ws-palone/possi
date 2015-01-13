package fr.istic.iodeman.model;

import java.util.Collection;

public class OralDefense {
	private Participant composition;
	private Room room;
	private TimeBox timebox;
	private Collection<Person> jury;
	
	
	public Participant getComposition() {
		return composition;
	}
	public void setComposition(Participant composition) {
		this.composition = composition;
	}
	public Room getRoom() {
		return room;
	}
	public void setRoom(Room room) {
		this.room = room;
	}
	public TimeBox getTimebox() {
		return timebox;
	}
	public void setTimebox(TimeBox timebox) {
		this.timebox = timebox;
	}
	public Collection<Person> getJury() {
		return jury;
	}
	public void setJury(Collection<Person> jury) {
		this.jury = jury;
	}
	
	
}