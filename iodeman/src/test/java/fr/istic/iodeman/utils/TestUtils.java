package fr.istic.iodeman.utils;

import java.util.Collection;

import org.joda.time.DateTime;

import fr.istic.iodeman.model.OralDefense;
import fr.istic.iodeman.model.TimeBox;

public class TestUtils {
	public static void printResults(Collection<OralDefense> results) {
		
		for(OralDefense oralDefense : results) {
			
			DateTime date1 = new DateTime(oralDefense.getTimebox().getFrom());
			DateTime date2 = new DateTime(oralDefense.getTimebox().getTo());
			
			System.out.println(
					"oral defense for " +
					oralDefense.getComposition().getStudent().getFirstName()
					+ " - " + oralDefense.getComposition().getFollowingTeacher().getFirstName()
					+ " set on " + date1.toString("dd/MM/yyyy HH:mm")
					+ " - " + date2.toString("dd/MM/yyyy HH:mm")
					+ " in " + oralDefense.getRoom().getName()
			);
			
		}
		
	}
	
	public static void printTimeBoxes(Collection<TimeBox> results) {
		
		for(TimeBox tb : results) {
			
			System.out.println(
					(new DateTime(tb.getFrom())).toString("dd/MM/yyyy HH:mm")
					+ " - " +
					(new DateTime(tb.getTo())).toString("dd/MM/yyyy HH:mm")
			);
			
		}
	}
}
