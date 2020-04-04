package fr.istic.iodeman.model;

import org.apache.commons.lang.Validate;

import javax.persistence.Embeddable;
import java.util.Date;

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

	@Override
	public boolean equals(Object o) {
		if (o instanceof TimeBox) {
			TimeBox timeBox = (TimeBox) o;
			return this.from.equals(timeBox.getFrom()) && this.to.equals(timeBox.getTo());
		}
		return false;
	}
}
