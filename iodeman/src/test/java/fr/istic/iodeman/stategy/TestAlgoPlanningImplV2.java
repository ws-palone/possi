package fr.istic.iodeman.stategy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import fr.istic.iodeman.model.OralDefense;
import fr.istic.iodeman.model.Participant;
import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.Priority;
import fr.istic.iodeman.model.Role;
import fr.istic.iodeman.model.Room;
import fr.istic.iodeman.model.TimeBox;
import fr.istic.iodeman.model.Unavailability;
import fr.istic.iodeman.strategy.AlgoPlanningImplV2;
import fr.istic.iodeman.strategy.AlgoPlanningV2;
import fr.istic.iodeman.utils.TestUtils;

public class TestAlgoPlanningImplV2 {

	private AlgoPlanningV2 algo;
	
	@Before
	public void setUp() {
		
		// implementation that will be tested
		algo = new AlgoPlanningImplV2();
	
	}
	
	private void checkResults(Collection<OralDefense> results, Collection<Participant> participants, Collection<Unavailability> unavailabilities) {
		
		// verify that there is the same number of participants than generated oral defenses
		assertEquals(participants.size(), results.size());
		
		List<Participant> finalParticipants = Lists.newArrayList();
		Map<TimeBox, List<Room>> finalBoxes = Maps.newHashMap();
		
		for(OralDefense oralDefense : results) {
			
			// verify if a participant is not present twice
			Participant p = oralDefense.getComposition();
			assertFalse(finalParticipants.contains(p));
			finalParticipants.add(p);
			
			// verify that there is only one oral defense for a couple (TimeBox, Room)
			List<Room> allocatedRooms = finalBoxes.get(oralDefense.getTimebox());
			if (allocatedRooms != null) {
				assertFalse(allocatedRooms.contains(oralDefense.getRoom()));
				allocatedRooms.add(oralDefense.getRoom());
			}else{
				finalBoxes.put(oralDefense.getTimebox(), Lists.newArrayList(oralDefense.getRoom()));
			}

		}
		
		// verify that the given unavailabilities have been respected
		for(Unavailability ua : unavailabilities) {
			assertTrue("Unavailability not respected", TestUtils.checkIfUnavailabilityRespected(results, ua));
		}
		
	}

	@Test
	public void testOk2() {
		
		List<Participant> participants = TestUtils.createParticipants(3);
		
		Room room1 = new Room();
		room1.setName("i227");
		
		Priority priority1 = new Priority();
		priority1.setRole(Role.STUDENT);
		priority1.setWeight(1);
		
		Priority priority2 = new Priority();
		priority2.setWeight(10);
		priority2.setRole(Role.PROF);
		
		Planning planning = new Planning();
		planning.setParticipants(participants);
		planning.setRooms(Lists.newArrayList(room1));
		planning.setPriorities(Lists.newArrayList(priority1, priority2));
		
		List<TimeBox> timeBoxes = TestUtils.createTimeBoxes(4);
		
		List<Unavailability> unavailabilities = Lists.newArrayList();
		
		Unavailability ua1 = new Unavailability();
		ua1.setPerson(participants.get(1).getStudent());
		ua1.setPeriod(new TimeBox(
				(new DateTime(2015,1,13,10,0)).toDate(),
				(new DateTime(2015,1,13,12,0)).toDate()
		));
		unavailabilities.add(ua1);
		
		Unavailability ua2 = new Unavailability();
		ua2.setPerson(participants.get(2).getFollowingTeacher());
		ua2.setPeriod(new TimeBox(
				(new DateTime(2015,1,13,8,0)).toDate(),
				(new DateTime(2015,1,13,9,0)).toDate()
		));
		unavailabilities.add(ua2);
		
		Unavailability ua3 = new Unavailability();
		ua3.setPerson(participants.get(2).getFollowingTeacher());
		ua3.setPeriod(new TimeBox(
				(new DateTime(2015,1,13,11,0)).toDate(),
				(new DateTime(2015,1,13,12,0)).toDate()
		));
		unavailabilities.add(ua3);
		
		Unavailability ua4 = new Unavailability();
		ua4.setPerson(participants.get(0).getFollowingTeacher());
		ua4.setPeriod(new TimeBox(
				(new DateTime(2015,1,13,9,0)).toDate(),
				(new DateTime(2015,1,13,10,0)).toDate()
		));
		unavailabilities.add(ua4);
		
		Unavailability ua5 = new Unavailability();
		ua5.setPerson(participants.get(0).getFollowingTeacher());
		ua5.setPeriod(new TimeBox(
				(new DateTime(2015,1,13,11,0)).toDate(),
				(new DateTime(2015,1,13,12,0)).toDate()
		));
		unavailabilities.add(ua5);
		
		algo.configure(planning, planning.getParticipants(), timeBoxes, unavailabilities);
		
		Collection<OralDefense> results = algo.execute();
		
		TestUtils.printResults(results);
		
		// verification
		checkResults(results, participants, unavailabilities);
		
	}
	
	@Test
	public void testOK1() {
		
		List<Participant> participants = TestUtils.createParticipants(14);
		
		Room room1 = new Room();
		room1.setName("i227");
		Room room2 = new Room();
		room2.setName("i58");
		
		Priority priority1 = new Priority();
		priority1.setRole(Role.STUDENT);
		priority1.setWeight(1);
		
		Priority priority2 = new Priority();
		priority2.setWeight(10);
		priority2.setRole(Role.PROF);
		
		Planning planning = new Planning();
		planning.setParticipants(participants);
		planning.setRooms(Lists.newArrayList(room1, room2));
		planning.setPriorities(Lists.newArrayList(priority1, priority2));
		
		List<TimeBox> timeBoxes = TestUtils.createTimeBoxes(8);
		
		List<Unavailability> unavailabilities = Lists.newArrayList();
		
		Unavailability ua1 = new Unavailability();
		ua1.setPerson(participants.get(0).getStudent());
		ua1.setPeriod(new TimeBox(
				(new DateTime(2015,1,13,8,0)).toDate(),
				(new DateTime(2015,1,13,12,0)).toDate()
		));
		unavailabilities.add(ua1);
		
		Unavailability ua2 = new Unavailability();
		ua2.setPerson(participants.get(1).getFollowingTeacher());
		ua2.setPeriod(new TimeBox(
				(new DateTime(2015,1,13,12,0)).toDate(),
				(new DateTime(2015,1,13,15,0)).toDate()
		));
		unavailabilities.add(ua2);
		
		Unavailability ua3 = new Unavailability();
		ua3.setPerson(participants.get(2).getFollowingTeacher());
		ua3.setPeriod(new TimeBox(
				(new DateTime(2015,1,13,10,0)).toDate(),
				(new DateTime(2015,1,13,12,0)).toDate()
		));
		unavailabilities.add(ua3);
		
		algo.configure(planning, planning.getParticipants(), timeBoxes, unavailabilities);
		
		Collection<OralDefense> results = algo.execute();
		
		TestUtils.printResults(results);
		
		// verification
		checkResults(results, participants, unavailabilities);
		
	}
	
	@Test
	public void testOKWithoutUnavailabilities() {
		
		List<Participant> participants = TestUtils.createParticipants(14);
		
		Room room1 = new Room();
		room1.setName("i227");
		Room room2 = new Room();
		room2.setName("i58");
		
		Planning planning = new Planning();
		planning.setParticipants(participants);
		planning.setRooms(Lists.newArrayList(room1, room2));
		
		List<TimeBox> timeBoxes = TestUtils.createTimeBoxes(8);
		
		algo.configure(planning, planning.getParticipants(), timeBoxes, null);
		
		Collection<OralDefense> results = algo.execute();
		
		TestUtils.printResults(results);
		
		Collection<Unavailability> unavailabilities = Lists.newArrayList();
		
		// verification
		checkResults(results, participants, unavailabilities);
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testConfigureWithNullPlanning() {
		
		Collection<Unavailability> unavailabilities = Lists.newArrayList();
		
		algo.configure(null, TestUtils.createParticipants(4), TestUtils.createTimeBoxes(5), unavailabilities);
	
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testExecuteWithoutConfigure() {
		
		algo.execute();
		
	}
	
	
}
