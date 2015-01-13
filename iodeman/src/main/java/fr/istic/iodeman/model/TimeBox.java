package fr.istic.iodeman.model;

import java.util.Date;

public class TimeBox {
	
	private Date from;
	private Date to;
	
	public TimeBox() {
		
	}
	
	public TimeBox(Date from, Date to) {
		setFrom(from);
		setTo(to);
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
