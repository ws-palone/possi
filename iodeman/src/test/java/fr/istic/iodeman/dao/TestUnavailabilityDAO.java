package fr.istic.iodeman.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;

import fr.istic.iodeman.model.Participant;
import fr.istic.iodeman.model.Person;
import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.TimeBox;
import fr.istic.iodeman.model.Unavailability;
import fr.istic.iodeman.utils.AbstractSpringUnitTest;

public class TestUnavailabilityDAO extends AbstractSpringUnitTest {

	@Autowired
	UnavailabilityDAO unavailabilityDAO;
	
	@Autowired
	PlanningDAO planningDAO;
	
	@Autowired
	PersonDAO personDAO;
	
	@Autowired
	ParticipantDAO participantDAO;
	
	List<Person> persons;

	List<Unavailability> unavailabilities;
	
	List<Planning> plannings;
	
	@Before
	public void setUp(){		
		
		persons = new ArrayList<Person>();
		unavailabilities = new ArrayList<Unavailability>();
		plannings = new ArrayList<Planning>();
		
		Person p8 = new Person();p8.setUid("11008880");persons.add(p8);
		Person p9 = new Person();p9.setUid("foursovM");persons.add(p9);
		
		for(Person p : persons){
			personDAO.persist(p);
		}
		
		Participant pa1 = new Participant();pa1.setFollowingTeacher(p9);pa1.setStudent(p8);
		
		Collection<Participant> participants1 = Lists.newArrayList(pa1);
		
		Planning pl1 = new Planning();pl1.setParticipants(participants1);pl1.setAdmin(p9);
		plannings.add(pl1);
		
		for(Planning p : plannings){
			planningDAO.persist(p);
		}
		
		TimeBox period = new TimeBox(
				new DateTime(2015, 8, 15, 0, 0).toDate(),
				new DateTime(2015, 8, 17, 0, 0).toDate()
		);

		Unavailability u1 = new Unavailability();u1.setPerson(p8);u1.setPlanning(pl1);u1.setPeriod(period);unavailabilities.add(u1);
			
		for(Unavailability p : unavailabilities){
			unavailabilityDAO.persist(p);
		}
		
	}
	
	@After
	public void after(){

		unavailabilityDAO.deleteAll();
		planningDAO.deleteAll();
		participantDAO.deleteAll();
		personDAO.deleteAll();

		assertEquals(unavailabilityDAO.findAll().size(),0);		
	}
	
	
	@Test
	public void testFindAll() {
		// we test if the number of retrieved Unavailability is the same that the added ones
		List<Unavailability> retrievedUnavailabilities = unavailabilityDAO.findAll();
		assertTrue(retrievedUnavailabilities.size() == unavailabilities.size());		
		
	}
	
	@Test
	public void testFindById(){
		Unavailability p = unavailabilities.get(0);
		assertTrue(p.getId() != null);
		
		Unavailability retrievedUnavailability = unavailabilityDAO.findById(p.getId());
		assertEquals(retrievedUnavailability.getId(), p.getId());
	}
	
	@Test
	public void findById(){
		Unavailability u = unavailabilityDAO.findAll().get(0);
		
		DateTime dt = new DateTime(2015, 8, 15, 0, 0);
		DateTime dtDAO = new DateTime(u.getPeriod().getFrom());
		
		
		assertEquals(dtDAO, dt);
	}

}
