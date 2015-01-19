package fr.istic.iodeman.stategy;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
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
import static org.junit.Assert.*;

public class TestAlgoJuryAssignation {

	private AlgoJuryAssignation algo;
	private List<OralDefense> oralDefenses;
	
	@Before
	public void setUp() {
		
		oralDefenses = Lists.newArrayList();
		
		Room room1 = new Room();
		room1.setName("i58");
		Room room2 = new Room();
		room2.setName("i227");
		
		int nb = 6;
		List<Participant> participants = TestUtils.createParticipants(nb);
		List<TimeBox> timeBoxes = TestUtils.createTimeBoxes(nb);
		
		int i = 0;
		int j = 0;
		while(i < nb) {
			OralDefense od = new OralDefense();
			od.setComposition(participants.get(i));
			od.setTimebox(timeBoxes.get(j));
			if (i%2 == 0) {
				j++;
				od.setRoom(room1);
			}else{
				od.setRoom(room2);
			}
			i++;
			oralDefenses.add(od);
		}
		
		
		algo = new AlgoJuryAssignationImpl();
		
	}
	
	@Test
	public void testOk1() {
		
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
		
		TestUtils.printResults(results);
		
		checkResults(results, unavailabilities);
		
	}
	
	private void checkResults(Collection<OralDefense> results, List<Unavailability> unavailabilities) {
		
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
