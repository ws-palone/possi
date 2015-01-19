package fr.istic.iodeman.model;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table
public class OralDefense implements Serializable {
	
	@Id
	@GeneratedValue
	@Column
	private Integer id;
	@Column
	private Participant composition;
	@Column
	private Room room;
	@Column
	private TimeBox timebox;
	//@OneToMany(mappedBy = "person")
	@Transient
	private Collection<Person> jury;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
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
