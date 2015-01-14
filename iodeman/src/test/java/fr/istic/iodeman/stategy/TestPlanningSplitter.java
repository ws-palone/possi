package fr.istic.iodeman.stategy;

import static org.junit.Assert.*;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;

import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.TimeBox;
import fr.istic.iodeman.strategy.PlanningSplitter;
import fr.istic.iodeman.strategy.PlanningSplitterImpl;

public class TestPlanningSplitter {

	@Test
	public void test1() {
		
		Planning planning = new Planning();
		planning.setPeriod(new TimeBox(
				new DateTime(2015, 1, 15, 0, 0).toDate(),
				new DateTime(2015, 1, 16, 0, 0).toDate()
		));
		planning.setDayPeriod(new TimeBox(
				new DateTime(2015, 1, 18, 8, 0).toDate(),
				new DateTime(2015, 1, 18, 18, 15).toDate()
		));
		planning.setLunchBreak(new TimeBox(
				new DateTime(2015, 1, 18, 12, 0).toDate(),
				new DateTime(2015, 1, 18, 14, 0).toDate()
		));
		planning.setOralDefenseDuration(20);
		planning.setOralDefenseInterlude(5);
		
		PlanningSplitter splitter = new PlanningSplitterImpl();
		List<TimeBox> results = splitter.execute(planning);
	
		System.out.println("Timeboxes generated: "+results.size());
		
		for(TimeBox tb : results) {
			
			System.out.println(
					(new DateTime(tb.getFrom())).toString("dd/MM/yyyy HH:mm")
					+ " - " +
					(new DateTime(tb.getTo())).toString("dd/MM/yyyy HH:mm")
			);
			
		}
		
		
	}
	
}
