package fr.istic.iodeman.dto;

import java.util.Collection;

import fr.istic.iodeman.model.TimeBox;

public class AgendaDTO {
	private TimeBox timebox;
	private Collection<DayItem> days;
		
	public TimeBox getTimebox() {
		return timebox;
	}

	public void setTimebox(TimeBox timebox) {
		this.timebox = timebox;
	}

	public Collection<DayItem> getDays() {
		return days;
	}

	public void setDays(Collection<DayItem> days) {
		this.days = days;
	}

	private class DayItem{
		String day;
		Boolean checked;
		
		public String getDay() {
			return day;
		}
		public void setDay(String day) {
			this.day = day;
		}
		public Boolean getChecked() {
			return checked;
		}
		public void setChecked(Boolean checked) {
			this.checked = checked;
		}		
	}
	
}
