package fr.istic.iodeman.strategy;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.joda.time.DateTime;
import org.joda.time.Days;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.TimeBox;
import fr.istic.iodeman.utils.AlgoPlanningUtils;

public class PlanningSplitterImpl implements PlanningSplitter {

	Planning planning;
	List<TimeBox> results;
	
	public List<TimeBox> execute(Planning planning) {
		
		Validate.notNull(planning);
		Validate.notNull(planning.getPeriod());
		Validate.notNull(planning.getDayPeriod());
		Validate.notNull(planning.getOralDefenseDuration());
		
		this.planning = planning;
		results = Lists.newArrayList();
		
		Integer duration = planning.getOralDefenseDuration();
		
		Validate.isTrue(duration > 0);
		
		Integer interlude = planning.getOralDefenseInterlude();
		
		if (interlude == null) {
			interlude = 0;
		}
		
		TimeBox period = planning.getPeriod();
		TimeBox dayPeriod = planning.getDayPeriod();
		TimeBox lunchBreak = planning.getLunchBreak();
		
		DateTime periodDateFrom = new DateTime(period.getFrom()).withTimeAtStartOfDay();
		DateTime periodDateTo = new DateTime(period.getTo()).withTimeAtStartOfDay();
		
		Integer nbDays = Days.daysBetween(periodDateFrom, periodDateTo).getDays() + 1;
		
		System.out.println("PlanningSplitter execution...");
		System.out.println("Days found in period: "+nbDays);
		
		for(int i=0; i<nbDays; i++) {
			
			DateTime dateFrom = periodDateFrom
					.plusDays(i)
					.plusMinutes(new DateTime(dayPeriod.getFrom()).minuteOfDay().get());
			
			DateTime dateLimit = periodDateFrom
					.plusDays(i)
					.plusMinutes(new DateTime(dayPeriod.getTo()).minuteOfDay().get());

			DateTime dateTo = dateFrom.plusMinutes(duration);	
			System.out.println(dateFrom.toString());
			System.out.println(dateTo.toString());
			
			while (!dateTo.isAfter(dateLimit)) {
				
				TimeBox tb = new TimeBox(dateFrom.toDate(), dateTo.toDate());
				
				// verify that the timebox is not in the lunch break period
				if (isNotOnLunchBreak(lunchBreak, tb)) {
					
					// add the timebox to the result
					results.add(tb);	
					
					// set the date of the beginning of the next timebox
					dateFrom = dateTo;
					
					// add the interlude time to this date if it exists
					if (interlude != null && interlude > 0) {
						dateFrom = dateFrom.plusMinutes(interlude);
					}
					
				}else{
					
					// set the date of the begining of the next timebox to the end of the lunch break
					dateFrom = dateFrom
							.withTimeAtStartOfDay()
							.plusMinutes(new DateTime(lunchBreak.getTo()).minuteOfDay().get());
	
				}
				
				// set the date of the end of the timebox
				dateTo = dateFrom.plusMinutes(duration);
				
			}
			
		}
		
		return Lists.newArrayList(Collections2.filter(results, new Predicate<TimeBox>(){

			@Override
			public boolean apply(TimeBox tb) {
				DateTime from = new DateTime(tb.getFrom());
				if (from.getDayOfWeek() == 6 || from.getDayOfWeek() == 7){
					return false;
				}
				return true;
			}
			
		}));
		
	}
	
	private boolean isNotOnLunchBreak(TimeBox lunchBreak, TimeBox timeBox) {
		
		if (lunchBreak != null && timeBox != null) {
			
			TimeBox lunchPeriod = new TimeBox(
					(new DateTime(timeBox.getFrom()))
						.withTimeAtStartOfDay()
						.plusMinutes(new DateTime(lunchBreak.getFrom()).minuteOfDay().get())
						.toDate(),
					(new DateTime(timeBox.getFrom()))
						.withTimeAtStartOfDay()
						.plusMinutes(new DateTime(lunchBreak.getTo()).minuteOfDay().get())
						.toDate()
			);
			
			return AlgoPlanningUtils.isAvailable(lunchPeriod, timeBox);
		
		}
		
		return true;
		
	}

}
