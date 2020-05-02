package fr.istic.iodeman.strategy;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import fr.istic.iodeman.models.Planning;
import fr.istic.iodeman.models.TimeBox;
import fr.istic.iodeman.utils.TimeBoxHelper;
import org.apache.commons.lang.Validate;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.List;
import java.util.stream.Collectors;

public class PlanningSplitterImpl implements PlanningSplitter {

	Planning planning;
	List<TimeBox> timeBoxes;
	private int nbDays;

	@Override
	public PlanningSplitter execute(Planning planning) {

		Validate.notNull(planning);
		Validate.notNull(planning.getPeriod());
		Validate.notNull(planning.getDayPeriod());
		Validate.notNull(planning.getOralDefenseDuration());

		this.planning = planning;
		timeBoxes = Lists.newArrayList();

		Integer duration = planning.getOralDefenseDuration();

		Validate.isTrue(duration > 0);

		Integer interlude = planning.getOralDefenseInterlude();

		if (interlude == null) {
			interlude = 0;
		}

		TimeBox period = planning.getPeriod();
		TimeBox dayPeriod = planning.getDayPeriod();

		DateTime periodDateFrom = new DateTime(period.getFrom()).withTimeAtStartOfDay();
		DateTime periodDateTo = new DateTime(period.getTo()).withTimeAtStartOfDay();

		nbDays = Days.daysBetween(periodDateFrom, periodDateTo).getDays() + 1;

		System.out.println("PlanningSplitter execution...");
		System.out.println("Days found in period: "+ nbDays);

		for(int i = 0; i< nbDays; i++) {

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
				// add the timebox to the result
				timeBoxes.add(tb);

				// set the date of the beginning of the next timebox
				dateFrom = dateTo;

				// add the interlude time to this date if it exists
				if (interlude != null && interlude > 0) {
					dateFrom = dateFrom.plusMinutes(interlude);
				}
				dateTo = dateFrom.plusMinutes(duration);
			}

		}

		this.timeBoxes =  Lists.newArrayList(Collections2.filter(timeBoxes, tb -> {
			assert tb != null;
			DateTime from = new DateTime(tb.getFrom());
			return from.getDayOfWeek() != 6 && from.getDayOfWeek() != 7;
		}));

		return this;
	}

	@Override
	public List<TimeBox> getTimeBoxes() {
		return timeBoxes;
	}

	@Override
	public List<TimeBox> getTimeBoxesWithoutLunchBreak() {
		return timeBoxes.stream().filter(t -> !TimeBoxHelper.isOnLunchBreak(planning.getLunchBreak(), t)).collect(Collectors.toList());
	}

	@Override
	public List<TimeBox> getLunchBreakTimeBoxes() {
		return timeBoxes.stream().filter(t -> TimeBoxHelper.isOnLunchBreak(planning.getLunchBreak(), t)).collect(Collectors.toList());
	}

	@Override
	public int getNbDays() {
		return nbDays;
	}
}
