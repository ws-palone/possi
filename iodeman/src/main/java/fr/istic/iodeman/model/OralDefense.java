package fr.istic.iodeman.model;

import java.util.Collection;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table
public class OralDefense {
	
	@Id
	@GeneratedValue
	private Integer id;
	
	@OneToOne
	private Participant composition;
	
	@ManyToOne
	private Room room;
	
	@Embedded
	@AttributeOverrides( {
		@AttributeOverride(name = "from", column = @Column(name = "period_from")),
		@AttributeOverride(name = "to", column = @Column(name = "period_to"))
	})
	private TimeBox timebox;

	@OneToMany
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
