package fr.istic.iodeman.model;

import javax.persistence.*;
import java.util.Collection;


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
