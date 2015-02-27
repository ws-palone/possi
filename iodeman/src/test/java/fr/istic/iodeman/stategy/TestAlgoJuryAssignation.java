package fr.istic.iodeman.stategy;

import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import fr.istic.iodeman.model.OralDefense;
import fr.istic.iodeman.model.Participant;
import fr.istic.iodeman.model.Person;
import fr.istic.iodeman.model.Room;
import fr.istic.iodeman.model.TimeBox;
import fr.istic.iodeman.model.Unavailability;
import fr.istic.iodeman.strategy.AlgoJuryAssignation;
import fr.istic.iodeman.strategy.AlgoJuryAssignationImpl;
import fr.istic.iodeman.utils.AlgoPlanningUtils;
import fr.istic.iodeman.utils.TestUtils;

public class TestAlgoJuryAssignation {

	private Integer nbParticipants;
	
	private AlgoJuryAssignation algo;
	
	private List<OralDefense> oralDefenses;
	private List<Room> rooms;
	private List<Participant> participants;
	private List<TimeBox> timeBoxes;
	
	@Before
	public void setUp() {
		
		oralDefenses = Lists.newArrayList();
		
		Room room1 = new Room();
		room1.setName("i58");
		Room room2 = new Room();
		room2.setName("i227");
		
		rooms = Lists.newArrayList(room1, room2);
		
		nbParticipants = 6;
		participants = TestUtils.createParticipants(nbParticipants);
		timeBoxes = TestUtils.createTimeBoxes(nbParticipants);
		
		algo = new AlgoJuryAssignationImpl();
		
	}
	
	@Ignore
	public void testOk1() {
		
		int i = 0;
		int j = 0;
		while(i < nbParticipants) {
			OralDefense od = new OralDefense();
			od.setComposition(participants.get(i));
			od.setTimebox(timeBoxes.get(j));
			if (i%2 == 0) {
				j++;
				od.setRoom(rooms.get(0));
			}else{
				od.setRoom(rooms.get(1));
			}
			i++;
			oralDefenses.add(od);
		}
		
		List<Unavailability> unavailabilities = Lists.newArrayList();
		
		Unavailability ua1 = new Unavailability();
		ua1.setPerson(oralDefenses.get(1).getComposition().getFollowingTeacher());
		ua1.setPeriod(new TimeBox(
				(new DateTime(2015,1,13,10,0)).toDate(),
				(new DateTime(2015,1,13,12,0)).toDate()
		));
		unavailabilities.add(ua1);
		
		Unavailability ua2 = new Unavailability();
		ua2.setPerson(oralDefenses.get(2).getComposition().getFollowingTeacher());
		ua2.setPeriod(new TimeBox(
				(new DateTime(2015,1,13,8,0)).toDate(),
				(new DateTime(2015,1,13,9,0)).toDate()
		));
		unavailabilities.add(ua2);
		
		Unavailability ua3 = new Unavailability();
		ua3.setPerson(oralDefenses.get(2).getComposition().getFollowingTeacher());
		ua3.setPeriod(new TimeBox(
				(new DateTime(2015,1,13,11,0)).toDate(),
				(new DateTime(2015,1,13,12,0)).toDate()
		));
		unavailabilities.add(ua3);
		
		algo.configure(oralDefenses, unavailabilities);
		
		Collection<OralDefense> results = algo.execute();
		
		checkResults(results, unavailabilities);
		
	}
	
	@Test
	public void testWithoutUnavailabilities() {
		
		int i = 0;
		int j = 0;
		while(i < nbParticipants) {
			OralDefense od = new OralDefense();
			od.setComposition(participants.get(i));
			od.setTimebox(timeBoxes.get(j));
			if (i%2 == 0) {
				j++;
				od.setRoom(rooms.get(0));
			}else{
				od.setRoom(rooms.get(1));
			}
			i++;
			oralDefenses.add(od);
		}
		
		algo.configure(oralDefenses, null);
		
		Collection<OralDefense> results = algo.execute();
		
		List<Unavailability> unavailabilities = Lists.newArrayList();
		
		checkResults(results, unavailabilities);
		
	}
	
	@Test
	public void testOnEmptyList() {
		
		oralDefenses = Lists.newArrayList();
		
		algo.configure(oralDefenses, null);
		
		Collection<OralDefense> results = algo.execute();
		
		List<Unavailability> unavailabilities = Lists.newArrayList();
		
		checkResults(results, unavailabilities);
		
	}
	
	@Test
	public void testOk2() {
		
		OralDefense od = new OralDefense();
		od.setComposition(participants.get(0));
		od.setTimebox(timeBoxes.get(0));
		od.setRoom(rooms.get(0));
		oralDefenses.add(od);
		
		od = new OralDefense();
		od.setComposition(participants.get(1));
		od.setTimebox(timeBoxes.get(1));
		od.setRoom(rooms.get(0));
		oralDefenses.add(od);
		
		Participant p = new Participant();
		p.setFollowingTeacher(participants.get(0).getFollowingTeacher());
		p.setStudent(participants.get(2).getStudent());
		
		od = new OralDefense();
		od.setComposition(p);
		od.setTimebox(timeBoxes.get(2));
		od.setRoom(rooms.get(0));
		oralDefenses.add(od);
		
		algo.configure(oralDefenses, null);
		
		Collection<OralDefense> results = algo.execute();
		
		List<Unavailability> unavailabilities = Lists.newArrayList();
		
		checkResults(results, unavailabilities);
		
	}
	
	public static void checkResults(Collection<OralDefense> results, List<Unavailability> unavailabilities) {
		
		TestUtils.printResults(results);
		
		for(OralDefense od : results) {
			
			assertTrue(od.getJury() != null && !od.getJury().isEmpty());
			
			final Person jury = Lists.newArrayList(od.getJury()).get(0);
			
			Collection<Unavailability> uaJury = Collections2.filter(unavailabilities, new Predicate<Unavailability>() {
				public boolean apply(Unavailability input) {
					return (input.getPerson().equals(jury));
				}
			});
			
			if (!uaJury.isEmpty()) {
				assertTrue(AlgoPlanningUtils.isAvailable(uaJury, od.getTimebox()));
			}
			
			Collection<OralDefense> othersAssignations = Collections2.filter(results, new Predicate<OralDefense>() {

				public boolean apply(OralDefense oralDefense) {
					return oralDefense.getComposition().getFollowingTeacher().equals(jury)
							|| oralDefense.getJury().contains(jury);
				}
			});
			
			for(OralDefense odAssignated : othersAssignations) {
				if (od != odAssignated) {
				assertTrue("a jury is assignated to 2 oral defenses on the same timebox!", 
						!odAssignated.getTimebox().equals(od.getTimebox()));
			
				}
			}
			
		}
		
	}
	
}
