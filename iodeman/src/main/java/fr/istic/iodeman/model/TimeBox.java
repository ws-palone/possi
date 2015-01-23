package fr.istic.iodeman.model;

import java.io.Serializable;
import java.util.Date;

public class TimeBox implements Serializable{
	
	private int id;
	private Date from;
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
