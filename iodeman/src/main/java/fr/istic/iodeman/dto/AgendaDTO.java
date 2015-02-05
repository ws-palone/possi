package fr.istic.iodeman.dto;

import java.util.ArrayList;
import java.util.Collection;

import fr.istic.iodeman.model.TimeBox;

public class AgendaDTO {
	private String line;
	private Collection<DayItem> days;
	
	public AgendaDTO(){
		days = new ArrayList<DayItem>();
	}

	public Collection<DayItem> getDays() {
		return days;
	}

	public void setDays(Collection<DayItem> days) {
		this.days = days;
	}
	
	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public void addDayItem(String day, Boolean checked, TimeBox timebox){
		DayItem d = new DayItem();
		d.setDay(day);
		d.setChecked(checked);
		d.setTimebox(timebox);
		days.add(d);
	}

	private class DayItem{
		String day;
		Boolean checked;
		TimeBox timebox;
		
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
		public TimeBox getTimebox() {
			return timebox;
		}
		public void setTimebox(TimeBox timebox) {
			this.timebox = timebox;
		}		
		
	}
	
}
