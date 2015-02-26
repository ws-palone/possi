package fr.istic.iodeman.model;

import java.util.Date;

import javax.persistence.Embeddable;

import org.apache.commons.lang.Validate;

@Embeddable
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
	
	public void validate() {
		
		Validate.notNull(from);
		Validate.notNull(to);
		Validate.isTrue(from.before(to) || from.equals(to));
	}
}
