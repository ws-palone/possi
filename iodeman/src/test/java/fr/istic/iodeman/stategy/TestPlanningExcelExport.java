package fr.istic.iodeman.stategy;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import fr.istic.iodeman.factory.OralDefenseFactory;
import fr.istic.iodeman.model.OralDefense;
import fr.istic.iodeman.model.Participant;
import fr.istic.iodeman.model.Person;
import fr.istic.iodeman.model.Role;
import fr.istic.iodeman.model.Room;
import fr.istic.iodeman.model.TimeBox;
import fr.istic.iodeman.strategy.PlanningExcelExport;
import fr.istic.iodeman.strategy.PlanningExport;
import fr.istic.iodeman.utils.TestUtils;

public class TestPlanningExcelExport {

	List<OralDefense> oralDefenses;
	List<TimeBox> timeBoxes;
	Collection<Room> rooms;

	@Before
	public void setUp(){
		oralDefenses = Lists.newArrayList();
		
		// timeboxes
		timeBoxes = Lists.newArrayList();
		
		timeBoxes.add(new TimeBox(
				(new DateTime(2015,1,14,11,0)).toDate(),
				(new DateTime(2015,1,14,12,0)).toDate()
		));
		timeBoxes.add(new TimeBox(
				(new DateTime(2015,1,13,8,0)).toDate(),
				(new DateTime(2015,1,13,9,0)).toDate()
				));
		timeBoxes.add(new TimeBox(
				(new DateTime(2015,1,13,9,0)).toDate(),
				(new DateTime(2015,1,13,10,0)).toDate()
		));
		timeBoxes.add(new TimeBox(
				(new DateTime(2015,1,14,10,0)).toDate(),
				(new DateTime(2015,1,14,11,0)).toDate()
		));
		timeBoxes.add(new TimeBox(
				(new DateTime(2015,1,14,9,0)).toDate(),
				(new DateTime(2015,1,14,10,0)).toDate()
		));
		timeBoxes.add(new TimeBox(
				(new DateTime(2015,1,13,10,0)).toDate(),
				(new DateTime(2015,1,13,11,0)).toDate()
		));
		timeBoxes.add(new TimeBox(
				(new DateTime(2015,1,13,11,0)).toDate(),
				(new DateTime(2015,1,13,12,0)).toDate()
		));
		timeBoxes.add(new TimeBox(
				(new DateTime(2015,1,14,8,0)).toDate(),
				(new DateTime(2015,1,14,9,0)).toDate()
		));
		
		/**
		 * BEGIN Rooms
		 */
		Room room1 = new Room();
		room1.setName("i50");
		Room room2 = new Room();
		room2.setName("i51");
		
		rooms = Lists.newArrayList();
		rooms.add(room1);
		rooms.add(room2);
		/**
		 * END Rooms
		 */
		
		/**
		 * BEGIN Participants
		 */
		List<Participant> participants = Lists.newArrayList();
		
		for(int i=1; i < 17; i++) {
			
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
		/**
		 * END participants
		 */
		
		/**
		 * Processing
		 */
		int i = 0;
		for(TimeBox timebox : timeBoxes){
			for(Room room : rooms){						
				// add it to the collection
				oralDefenses.add(OralDefenseFactory.createOralDefense(participants.get(i), room, timebox));
				i++;
			}
		}
				
//		TestUtils.printResults(oralDefenses);
		
	}

	@Test
	public void testExport() throws Exception{		
		PlanningExport export = new PlanningExcelExport();
		
		export.configure(createTimeBoxes(4));
		// reprendre le tri de date pour le test
		export.execute(oralDefenses);

		// testing if file exists
		File f = new File("/tmp/planning.xls");

		assertTrue(f.exists());
	}
	
	private List<TimeBox> createTimeBoxes(int nb) {
		
		List<TimeBox> timeBoxes = Lists.newArrayList();
		
		DateTime dateT = new DateTime(2015, 1, 13, 8, 0);
		
		while(timeBoxes.size() < nb) {	
			TimeBox tb = new TimeBox();
			tb.setFrom(dateT.toDate());
			dateT = dateT.plusHours(1);
			tb.setTo(dateT.toDate());
			timeBoxes.add(tb);
		}
		
		return timeBoxes;
	}
	
}
