package fr.istic.iodeman.stategy;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import fr.istic.iodeman.model.Participant;
import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.Room;
import fr.istic.iodeman.model.TimeBox;
import fr.istic.iodeman.strategy.PlanningDataValidator;
import fr.istic.iodeman.strategy.PlanningDataValidatorImpl;
import fr.istic.iodeman.utils.TestUtils;

public class TestPlanningDataValidator {

	private PlanningDataValidator validator;
	
	@Before
	public void setUp() {
		
		validator = new PlanningDataValidatorImpl();
		
	}
	
	@Test
	public void testOk() {
		
		Room r1 = new Room();
		r1.setName("i51");
		Room r2 = new Room();
		r2.setName("i227");
		
		Planning planning = new Planning();
		planning.setRooms(Lists.newArrayList(r1, r2));
		
		List<TimeBox> timeboxes = Lists.newArrayList(
				new TimeBox(
						new DateTime(2015,1,15,8,0).toDate(),
						new DateTime(2015,1,15,9,0).toDate()
				),
				new TimeBox(
						new DateTime(2015,1,15,10,0).toDate(),
						new DateTime(2015,1,15,11,0).toDate()
				),
				new TimeBox(
						new DateTime(2015,1,15,12,0).toDate(),
						new DateTime(2015,1,15,13,0).toDate()
				)
		);
		
		List<Participant> participants = TestUtils.createParticipants(6);
		
		validator.configure(planning, participants, timeboxes);
		validator.validate();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNoRoom() {
		
		List<Room> rooms =  Lists.newArrayList();
		
		Planning planning = new Planning();
		planning.setRooms(rooms);
		
		List<TimeBox> timeboxes = Lists.newArrayList(
				new TimeBox(
						new DateTime(2015,1,15,8,0).toDate(),
						new DateTime(2015,1,15,9,0).toDate()
				),
				new TimeBox(
						new DateTime(2015,1,15,10,0).toDate(),
						new DateTime(2015,1,15,11,0).toDate()
				),
				new TimeBox(
						new DateTime(2015,1,15,12,0).toDate(),
						new DateTime(2015,1,15,13,0).toDate()
				)
		);
		
		List<Participant> participants = TestUtils.createParticipants(6);
		
		validator.configure(planning, participants, timeboxes);
		validator.validate();
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNoTimeBox() {
		
		Room r1 = new Room();
		r1.setName("i51");
		Room r2 = new Room();
		r2.setName("i227");
		
		Planning planning = new Planning();
		planning.setRooms(Lists.newArrayList(r1, r2));
		
		List<TimeBox> timeboxes = Lists.newArrayList();
		
		List<Participant> participants = TestUtils.createParticipants(6);
		
		validator.configure(planning, participants, timeboxes);
		validator.validate();
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNoParticipant() {
		
		Room r1 = new Room();
		r1.setName("i51");
		Room r2 = new Room();
		r2.setName("i227");
		
		Planning planning = new Planning();
		planning.setRooms(Lists.newArrayList(r1, r2));
		
		List<TimeBox> timeboxes = Lists.newArrayList(
				new TimeBox(
						new DateTime(2015,1,15,8,0).toDate(),
						new DateTime(2015,1,15,9,0).toDate()
				),
				new TimeBox(
						new DateTime(2015,1,15,10,0).toDate(),
						new DateTime(2015,1,15,11,0).toDate()
				),
				new TimeBox(
						new DateTime(2015,1,15,12,0).toDate(),
						new DateTime(2015,1,15,13,0).toDate()
				)
		);
		
		List<Participant> participants = Lists.newArrayList();
		
		validator.configure(planning, participants, timeboxes);
		validator.validate();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNotTooManyParticipants() {
		
		Room r1 = new Room();
		r1.setName("i51");
		Room r2 = new Room();
		r2.setName("i227");
		
		Planning planning = new Planning();
		planning.setRooms(Lists.newArrayList(r1, r2));
		
		List<TimeBox> timeboxes = Lists.newArrayList(
				new TimeBox(
						new DateTime(2015,1,15,8,0).toDate(),
						new DateTime(2015,1,15,9,0).toDate()
				),
				new TimeBox(
						new DateTime(2015,1,15,10,0).toDate(),
						new DateTime(2015,1,15,11,0).toDate()
				),
				new TimeBox(
						new DateTime(2015,1,15,12,0).toDate(),
						new DateTime(2015,1,15,13,0).toDate()
				)
		);
		
		List<Participant> participants = TestUtils.createParticipants(7);
		
		validator.configure(planning, participants, timeboxes);
		validator.validate();
	}
	
	@Test
	public void testOkWithMaxOralDefenses() {
		
		Room r1 = new Room();
		r1.setName("i51");
		Room r2 = new Room();
		r2.setName("i227");
		
		Planning planning = new Planning();
		planning.setRooms(Lists.newArrayList(r1, r2));
		planning.setNbMaxOralDefensePerDay(2);
		
		List<TimeBox> timeboxes = Lists.newArrayList(
				new TimeBox(
						new DateTime(2015,1,15,8,0).toDate(),
						new DateTime(2015,1,15,9,0).toDate()
				),
				new TimeBox(
						new DateTime(2015,1,15,10,0).toDate(),
						new DateTime(2015,1,15,11,0).toDate()
				),
				new TimeBox(
						new DateTime(2015,1,15,12,0).toDate(),
						new DateTime(2015,1,15,13,0).toDate()
				),
				new TimeBox(
						new DateTime(2015,1,16,8,0).toDate(),
						new DateTime(2015,1,16,9,0).toDate()
				)
		);
		
		List<Participant> participants = TestUtils.createParticipants(4);
		
		validator.configure(planning, participants, timeboxes);
		validator.validate();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNotOkWithMaxOralDefenses() {
		
		Room r1 = new Room();
		r1.setName("i51");
		Room r2 = new Room();
		r2.setName("i227");
		
		Planning planning = new Planning();
		planning.setRooms(Lists.newArrayList(r1, r2));
		planning.setNbMaxOralDefensePerDay(2);
		
		List<TimeBox> timeboxes = Lists.newArrayList(
				new TimeBox(
						new DateTime(2015,1,15,8,0).toDate(),
						new DateTime(2015,1,15,9,0).toDate()
				),
				new TimeBox(
						new DateTime(2015,1,15,10,0).toDate(),
						new DateTime(2015,1,15,11,0).toDate()
				),
				new TimeBox(
						new DateTime(2015,1,15,12,0).toDate(),
						new DateTime(2015,1,15,13,0).toDate()
				),
				new TimeBox(
						new DateTime(2015,1,16,8,0).toDate(),
						new DateTime(2015,1,16,9,0).toDate()
				)
		);
		
		List<Participant> participants = TestUtils.createParticipants(6);
		
		validator.configure(planning, participants, timeboxes);
		validator.validate();
	}
	
	
}
