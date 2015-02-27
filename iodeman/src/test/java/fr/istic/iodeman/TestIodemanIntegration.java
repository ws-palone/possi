package fr.istic.iodeman;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;

import fr.istic.iodeman.dao.PersonDAO;
import fr.istic.iodeman.model.OralDefense;
import fr.istic.iodeman.model.Participant;
import fr.istic.iodeman.model.Person;
import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.Priority;
import fr.istic.iodeman.model.Role;
import fr.istic.iodeman.model.TimeBox;
import fr.istic.iodeman.model.Unavailability;
import fr.istic.iodeman.resolver.PersonMailResolver;
import fr.istic.iodeman.resolver.PersonResolver;
import fr.istic.iodeman.service.PlanningService;
import fr.istic.iodeman.service.RoomService;
import fr.istic.iodeman.service.UnavailabilityService;
import fr.istic.iodeman.utils.TestUtils;

public class IodemanIntegrationTest extends SpringUnitTest {

	@Autowired
	@InjectMocks
	private PlanningService planningService;
	
	@Autowired
	private PersonDAO personDAO;
	
	@Autowired
	private UnavailabilityService unavailabilityService;
	
	@Autowired
	private RoomService roomService;
	
	@Mock
	private PersonMailResolver personMailResolver;
	
	private PersonResolver personResolver;
	
	private class PersonResolverMock implements PersonResolver {

		public Person resolve(String email) {
			Person person = personDAO.findByEmail(email);
			
			if (person == null) {
				person = new Person();
				person.setFirstName(email);
				person.setUid(email);
				person.setEmail(email);
				if (!email.contains("@etudiant.univ-rennes1.fr")) {
					person.setRole(Role.PROF);
				}else{
					person.setRole(Role.STUDENT);
				}
				personDAO.persist(person);
			}

			return person;
		}
	}
	
	@Before
	public void setUp() {
		
		personResolver = new PersonResolverMock();
		
		MockitoAnnotations.initMocks(this);
		Mockito.when(personMailResolver.resolve(Matchers.anyString())).thenAnswer(new Answer<Person>() {
			@Override
			public Person answer(InvocationOnMock invocation) throws Throwable {
				String mail = invocation.getArgumentAt(0, String.class);
				return personResolver.resolve(mail);
			}
		});
	}
	
	@Test
	public void test() throws Exception {
		
		// Create a planning
		Planning planning = new Planning();
		planning.setName("IT Planning");
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
		planning.setRooms(
			Lists.newArrayList(
				roomService.findOrCreate("i51"),
				roomService.findOrCreate("i227")
			)	
		);
		
		Person admin = personResolver.resolve("13006394");
		
		// Create a new planning
		planning = planningService.create(
				admin, 
				planning.getName(), 
				planning.getPeriod(), 
				planning.getOralDefenseDuration(), 
				planning.getOralDefenseInterlude(),
				planning.getLunchBreak(), 
				planning.getDayPeriod(), 
				planning.getNbMaxOralDefensePerDay(), 
				planning.getRooms()
		);
		assertTrue(planning != null);
		
		// Update the priorities of the planning
		List<Priority> priorities = Lists.newArrayList(
				new Priority(Role.STUDENT, 1), 
				new Priority(Role.PROF, 3)
		);
		planningService.updatePriorities(planning, priorities);
		
		// Check if the priorities have been successfully updated
		planning = planningService.findById(planning.getId());
		assertEquals(2, planning.getPriorities().size());
		
		// Verify that the excel exits
		String filename = "/import_couple.xls";
		File excelFile = new File(getClass().getResource(filename).toURI());
		assertTrue(excelFile.exists());
		
		// Import participants from the excel file
		planning = planningService.importPartcipants(planning, excelFile);
		assertTrue(planning != null);
		
		// Verify if the participants have been imported
		List<Participant> participants = Lists.newArrayList(planningService.findParticipants(planning));
		assertTrue(participants.size() > 0);
		
		List<Unavailability> unavailabilities = Lists.newArrayList();
		
		Unavailability ua1 = unavailabilityService.create(
				planning.getId(), 
				participants.get(1).getStudent().getUid(),
				new TimeBox(
						(new DateTime(2015,1,15,10,0)).toDate(),
						(new DateTime(2015,1,15,12,0)).toDate()
				)
		);
		unavailabilities.add(ua1);
		
		Unavailability ua2 = unavailabilityService.create(
				planning.getId(), 
				participants.get(2).getFollowingTeacher().getUid(),
				new TimeBox(
						(new DateTime(2015,1,15,8,0)).toDate(),
						(new DateTime(2015,1,15,9,0)).toDate()
				)
		);
		unavailabilities.add(ua2);
		
		Unavailability ua3 = unavailabilityService.create(
				planning.getId(), 
				participants.get(2).getFollowingTeacher().getUid(),
				new TimeBox(
						(new DateTime(2015,1,15,11,0)).toDate(),
						(new DateTime(2015,1,15,12,0)).toDate()
				)
		);
		unavailabilities.add(ua3);
		
		Unavailability ua4 = unavailabilityService.create(
				planning.getId(), 
				participants.get(0).getFollowingTeacher().getUid(),
				new TimeBox(
						(new DateTime(2015,1,15,11,0)).toDate(),
						(new DateTime(2015,1,15,12,0)).toDate()
				)
		);
		unavailabilities.add(ua4);
		
		Unavailability ua5 = unavailabilityService.create(
				planning.getId(), 
				participants.get(3).getFollowingTeacher().getUid(),
				new TimeBox(
						(new DateTime(2015,1,15,11,0)).toDate(),
						(new DateTime(2015,1,15,12,0)).toDate()
				)
		);
		unavailabilities.add(ua5);
		
		Collection<OralDefense> oralDefenses = planningService.export(planning.getId());
		
		TestUtils.printResults(oralDefenses);
		
		for(Unavailability ua : unavailabilities) {
			assertTrue(TestUtils.checkIfUnavailabilityRespected(oralDefenses, ua));
		}
	}
	
	@Test
	public void test2() throws Exception {
		
		// Create a planning
		Planning planning = new Planning();
		planning.setName("IT Planning");
		planning.setPeriod(new TimeBox(
				new DateTime(2015, 1, 15, 0, 0).toDate(),
				new DateTime(2015, 1, 17, 0, 0).toDate()
		));
		planning.setDayPeriod(new TimeBox(
				new DateTime(2015, 1, 18, 9, 0).toDate(),
				new DateTime(2015, 1, 18, 17, 30).toDate()
		));
		planning.setLunchBreak(new TimeBox(
				new DateTime(2015, 1, 18, 12, 15).toDate(),
				new DateTime(2015, 1, 18, 14, 0).toDate()
		));
		planning.setOralDefenseDuration(30);
		planning.setOralDefenseInterlude(10);
		planning.setRooms(
			Lists.newArrayList(
				roomService.findOrCreate("i51"),
				roomService.findOrCreate("i227")
			)	
		);
		
		Person admin = personResolver.resolve("13006394");
		
		// Create a new planning
		planning = planningService.create(
				admin, 
				planning.getName(), 
				planning.getPeriod(), 
				planning.getOralDefenseDuration(), 
				planning.getOralDefenseInterlude(),
				planning.getLunchBreak(), 
				planning.getDayPeriod(), 
				planning.getNbMaxOralDefensePerDay(), 
				planning.getRooms()
		);
		assertTrue(planning != null);
		
		// Update the priorities of the planning
		List<Priority> priorities = Lists.newArrayList(
				new Priority(Role.STUDENT, 1), 
				new Priority(Role.PROF, 3)
		);
		planningService.updatePriorities(planning, priorities);
		
		// Check if the priorities have been successfully updated
		planning = planningService.findById(planning.getId());
		assertEquals(2, planning.getPriorities().size());
		
		// Verify that the excel exits
		String filename = "/import_couple_m2miage.xls";
		File excelFile = new File(getClass().getResource(filename).toURI());
		assertTrue(excelFile.exists());
		
		// Import participants from the excel file
		planning = planningService.importPartcipants(planning, excelFile);
		assertTrue(planning != null);
		
		// Verify if the participants have been imported
		List<Participant> participants = Lists.newArrayList(planningService.findParticipants(planning));
		assertTrue(participants.size() > 0);
		
		List<Unavailability> unavailabilities = Lists.newArrayList();
		
		Unavailability ua1 = unavailabilityService.create(
				planning.getId(), 
				participants.get(1).getStudent().getUid(),
				new TimeBox(
						(new DateTime(2015,1,15,10,0)).toDate(),
						(new DateTime(2015,1,15,12,0)).toDate()
				)
		);
		unavailabilities.add(ua1);
		
		Unavailability ua2 = unavailabilityService.create(
				planning.getId(), 
				participants.get(2).getFollowingTeacher().getUid(),
				new TimeBox(
						(new DateTime(2015,1,15,8,0)).toDate(),
						(new DateTime(2015,1,15,9,0)).toDate()
				)
		);
		unavailabilities.add(ua2);
		
		Unavailability ua3 = unavailabilityService.create(
				planning.getId(), 
				participants.get(2).getFollowingTeacher().getUid(),
				new TimeBox(
						(new DateTime(2015,1,15,11,0)).toDate(),
						(new DateTime(2015,1,15,12,0)).toDate()
				)
		);
		unavailabilities.add(ua3);
		
		Unavailability ua4 = unavailabilityService.create(
				planning.getId(), 
				participants.get(0).getFollowingTeacher().getUid(),
				new TimeBox(
						(new DateTime(2015,1,15,11,0)).toDate(),
						(new DateTime(2015,1,15,12,0)).toDate()
				)
		);
		unavailabilities.add(ua4);
		
		Unavailability ua5 = unavailabilityService.create(
				planning.getId(), 
				participants.get(3).getFollowingTeacher().getUid(),
				new TimeBox(
						(new DateTime(2015,1,15,11,0)).toDate(),
						(new DateTime(2015,1,15,12,0)).toDate()
				)
		);
		unavailabilities.add(ua5);
		
		Collection<OralDefense> oralDefenses = planningService.export(planning.getId());
		
		TestUtils.printResults(oralDefenses);
		
		for(Unavailability ua : unavailabilities) {
			assertTrue(TestUtils.checkIfUnavailabilityRespected(oralDefenses, ua));
		}
	}
	
}
