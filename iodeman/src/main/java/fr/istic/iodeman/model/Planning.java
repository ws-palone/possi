package fr.istic.iodeman.model;

import java.util.Collection;
import java.util.Iterator;

public class Planning {
	
	private TimeBox period;
	private Integer oralDefenseDuration;
	private Integer oralDefenseInterlude;
	private TimeBox lunchBreak;
	private TimeBox dayPeriod;
	private Integer nbMaxOralDefensePerDay;
	private Collection<Room> rooms;
	private Collection<Participant> participants;
	
	public TimeBox getPeriod() {
		return period;
	}
	public void setPeriod(TimeBox period) {
		this.period = period;
	}
	public Integer getOralDefenseDuration() {
		return oralDefenseDuration;
	}
	public void setOralDefenseDuration(Integer oralDefenseDuration) {
		this.oralDefenseDuration = oralDefenseDuration;
	}
	public Integer getOralDefenseInterlude() {
		return oralDefenseInterlude;
	}
	public void setOralDefenseInterlude(Integer oralDefenseInterlude) {
		this.oralDefenseInterlude = oralDefenseInterlude;
	}
	public TimeBox getLunchBreak() {
		return lunchBreak;
	}
	public void setLunchBreak(TimeBox lunchBreak) {
		this.lunchBreak = lunchBreak;
	}
	public TimeBox getDayPeriod() {
		return dayPeriod;
	}
	public void setDayPeriod(TimeBox dayPeriod) {
		this.dayPeriod = dayPeriod;
	}
	public Integer getNbMaxOralDefensePerDay() {
		return nbMaxOralDefensePerDay;
	}
	public void setNbMaxOralDefensePerDay(Integer nbMaxOralDefensePerDay) {
		this.nbMaxOralDefensePerDay = nbMaxOralDefensePerDay;
	}
	public Iterator<Room> getRooms() {
		return rooms.iterator();
	}
	public void setRooms(Collection<Room> rooms) {
		this.rooms = rooms;
	}
	public Iterator<Participant> getParticipants() {
		return participants.iterator();
	}
	public void setParticipants(Collection<Participant> participants) {
		this.participants = participants;
	}
	
	
	
}
