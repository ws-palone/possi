package fr.istic.iodeman.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table
public class TimeBox {
	
	@Id
	@GeneratedValue
	@Column
	private int id;
	@Column
	private Date from;
	@Column
	private Date to;
	
	public TimeBox() {
		
	}
	
	public TimeBox(Date from, Date to) {
		setFrom(from);
		setTo(to);
	}
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getFrom() {
		return from;
	}
	public void setFrom(Date from) {
		this.from = from;
	}
	public Date getTo() {
		return to;
	}
	public void setTo(Date to) {
		this.to = to;
	}
}
