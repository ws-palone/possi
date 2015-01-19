package fr.istic.iodeman.model;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table
public class Planning implements Serializable{
	
	@Id
	@GeneratedValue
	@Column
	private Integer id;
	@Column
	private TimeBox period;
	@Column
	private Integer oralDefenseDuration;
	@Column
	private Integer oralDefenseInterlude;
	@Column
	private TimeBox lunchBreak;
	@Column
	private TimeBox dayPeriod;
	@Column
	private Integer nbMaxOralDefensePerDay;
	@OneToMany(mappedBy = "planning")
	private Collection<Room> rooms;
	@OneToMany(mappedBy="planning")
	private Collection<Participant> participants;
	@OneToMany(mappedBy="planning")
	private Collection<Priority> priorities;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
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
	public Collection<Room> getRooms() {
		return rooms;
	}
	public void setRooms(Collection<Room> rooms) {
		this.rooms = rooms;
	}
	public Collection<Participant> getParticipants() {
		return participants;
	}
	public void setParticipants(Collection<Participant> participants) {
		this.participants = participants;
	}
	public Collection<Priority> getPriorities() {
		return priorities;
	}
	public void setPriorities(Collection<Priority> priorities) {
		this.priorities = priorities;
	}	
}
