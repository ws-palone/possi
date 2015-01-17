package fr.istic.iodeman.utils;

import java.util.Collection;
import java.util.List;

import org.joda.time.DateTime;

import com.google.common.collect.Lists;

import fr.istic.iodeman.model.OralDefense;
import fr.istic.iodeman.model.Participant;
import fr.istic.iodeman.model.Person;
import fr.istic.iodeman.model.Role;
import fr.istic.iodeman.model.TimeBox;
import fr.istic.iodeman.model.Unavailability;

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
	
	
	public static boolean checkIfUnavailabilityRespected(Collection<OralDefense> results, Unavailability ua) {
		
		for(OralDefense oralDefense : results) {
			
			if (ua.getPerson().equals(oralDefense.getComposition().getStudent())
					|| ua.getPerson().equals(oralDefense.getComposition().getFollowingTeacher())) {
				
				return AlgoPlanningUtils.isAvailable(ua, oralDefense.getTimebox());
				
			}
			
		}
		
		return true;
		
	}
	
	public static List<Participant> createParticipants(int nb) {
		
		List<Participant> participants = Lists.newArrayList();
		
		for(int i=1; i < nb+1; i++) {
			
			Person p1 = new Person();
			p1.setId(i);
			p1.setFirstName("Student "+i);
			p1.setRole(Role.STUDENT);
			
			Person p2 = new Person();
			p2.setId(i);
			p2.setFirstName("Prof "+i);
			p2.setRole(Role.PROF);		
			
			Participant participant = new Participant();
			participant.setStudent(p1);
			participant.setFollowingTeacher(p2);
			
			participants.add(participant);
			
		}
		
		return participants;
	}
}
