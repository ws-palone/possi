package fr.istic.iodeman.stategy;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Collection;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;

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
			p1.setUid(Integer.toString(i));
			p1.setFirstName("Student "+i);
			p1.setRole(Role.STUDENT);

			Person p2 = new Person();
			p2.setId(i);
			p2.setUid(Integer.toString(i));
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

		export.configure(createTimeBoxes(2, 4));
		// reprendre le tri de date pour le test
		File f = export.execute(oralDefenses);		

		assertTrue(f.exists());
		
		Workbook workbook = Workbook.getWorkbook(f);
		Sheet sheet = workbook.getSheet(0);
		
		// we verify if there is a title
		assertFalse(sheet.getCell(1, 1).getContents().equals(""));
		
		// verification of the data of the first timebox
		OralDefense od = oralDefenses.get(2);
		assertTrue(sheet.getCell(1, 3).getContents().equals(od.getComposition().getStudent().getFirstName() + " " +od.getComposition().getStudent().getLastName() ));
		assertTrue(sheet.getCell(1, 4).getContents().equals(od.getComposition().getFollowingTeacher().getFirstName() + " " + od.getComposition().getFollowingTeacher().getLastName()));
//		assertTrue(sheet.getCell(3, 5).getContents().equals(""));
//		assertTrue(sheet.getCell(3, 6).getContents().equals(""));
	}

	@Test
	public void testExportWithTimeBoxBlank() throws Exception{		
		// we remove an oral defense
		// oral defense for Student 4 - Prof 4 set on 13/01/2015 08:00 - 13/01/2015 09:00 in i51
		int size = oralDefenses.size();
		OralDefense od = oralDefenses.get(3);
		oralDefenses.remove(od);
		assertTrue(oralDefenses.size() ==  (size-1));

		// oral defense for Student 9 - Prof 9 set on 14/01/2015 09:00 - 14/01/2015 10:00 in i50
		size = oralDefenses.size();
		od = oralDefenses.get(7);
		oralDefenses.remove(od);
		assertTrue(oralDefenses.size() ==  (size-1));
		
		// oral defense for Student 16 - Prof 16 set on 14/01/2015 08:00 - 14/01/2015 09:00 in i51
		size = oralDefenses.size();
		od = oralDefenses.get(13);
		oralDefenses.remove(od);
		assertTrue(oralDefenses.size() ==  (size-1));
		
		PlanningExport export = new PlanningExcelExport();

		export.configure(createTimeBoxes(2, 4));
		// reprendre le tri de date pour le test
		File f = export.execute(oralDefenses);

		assertTrue(f.exists());
		
		Workbook workbook = Workbook.getWorkbook(f);
		Sheet sheet = workbook.getSheet(0);
		
		// we test if the removed oral defense is display on the excel sheet
		// student 4
		assertTrue(sheet.getCell(2, 3).getContents().equals(""));
		assertTrue(sheet.getCell(2, 4).getContents().equals(""));
		assertTrue(sheet.getCell(2, 5).getContents().equals(""));
		assertTrue(sheet.getCell(2, 6).getContents().equals(""));
		
		// student 9
		assertTrue(sheet.getCell(3, 7).getContents().equals(""));
		assertTrue(sheet.getCell(3, 8).getContents().equals(""));
		assertTrue(sheet.getCell(3, 9).getContents().equals(""));
		assertTrue(sheet.getCell(3, 10).getContents().equals(""));
		
		// student 16
		assertTrue(sheet.getCell(4, 3).getContents().equals(""));
		assertTrue(sheet.getCell(4, 4).getContents().equals(""));
		assertTrue(sheet.getCell(4, 5).getContents().equals(""));
		assertTrue(sheet.getCell(4, 6).getContents().equals(""));
		
		// we verify if there is a title
		assertFalse(sheet.getCell(1, 1).getContents().equals(""));
	}

	private List<TimeBox> createTimeBoxes(int nbDays, int nbTimeBoxPerDay) {

		List<TimeBox> timeBoxes = Lists.newArrayList();

		DateTime dateT = new DateTime(2015, 1, 13, 8, 0);		

		for (int i = 0;i<nbDays; i++){
			int j = 0;
			while(j < nbTimeBoxPerDay) {	
				TimeBox tb = new TimeBox();
				tb.setFrom(dateT.toDate());
				dateT = dateT.plusHours(1);
				tb.setTo(dateT.toDate());
				timeBoxes.add(tb);
				j++;
			}
			dateT = dateT.plusDays(1);
			dateT = dateT.minusHours(nbTimeBoxPerDay);
		}

		return timeBoxes;
	}

}
