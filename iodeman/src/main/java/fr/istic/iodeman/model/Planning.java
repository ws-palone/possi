package fr.istic.iodeman.model;

import java.util.Collection;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class Planning{
	
	@Id
	@GeneratedValue
	private Integer id;

	private String name;

	@Embedded
	@AttributeOverrides( {
		@AttributeOverride(name = "from", column = @Column(name = "period_from")),
		@AttributeOverride(name = "to", column = @Column(name = "period_to"))
	})
	private TimeBox period;

	private Integer oralDefenseDuration;

	private Integer oralDefenseInterlude;

	@Embedded
	@AttributeOverrides( {
		@AttributeOverride(name = "from", column = @Column(name = "lunchBreak_from")),
		@AttributeOverride(name = "to", column = @Column(name = "lunchBreak_to"))
	})
	private TimeBox lunchBreak;
	@Embedded
	@AttributeOverrides( {
		@AttributeOverride(name = "from", column = @Column(name = "dayPeriod_from")),
		@AttributeOverride(name = "to", column = @Column(name = "dayPeriod_to"))
	})
	private TimeBox dayPeriod;

	private Integer nbMaxOralDefensePerDay;
	
	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany
	private Collection<Room> rooms;
	
	@JsonIgnore
	@OneToMany(cascade = CascadeType.PERSIST)
	private Collection<Participant> participants;
	
	@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(cascade = CascadeType.PERSIST)
	private Collection<Priority> priorities;
	
	@JsonIgnore
	@OneToMany
	private Collection<OralDefense> oralDefenses;
	
	@ManyToOne
	private Person admin;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public Collection<OralDefense> getOralDefenses() {
		return oralDefenses;
	}
	public void setOralDefenses(Collection<OralDefense> oralDefenses) {
		this.oralDefenses = oralDefenses;
	}
	public Person getAdmin() {
		return admin;
	}
	public void setAdmin(Person admin) {
		this.admin = admin;
	}
	
	
}
