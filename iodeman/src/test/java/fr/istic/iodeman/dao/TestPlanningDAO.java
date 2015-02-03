package fr.istic.iodeman.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;

import fr.istic.iodeman.model.Participant;
import fr.istic.iodeman.model.Person;
import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.Priority;
import fr.istic.iodeman.utils.AbstractSpringUnitTest;

public class TestPlanningDAO extends AbstractSpringUnitTest {

	@Autowired
	PlanningDAO planningDAO;
	
	@Autowired
	PersonDAO personDAO;
	
	@Autowired
	ParticipantDAO participantDAO;

	List<Planning> plannings;
	
	List<Person> persons;
	
	@Before
	public void setUp(){
		// instanciate the DAO
		//planningDAO = new PlanningDAOImpl();
		
		
		
		// creation of a list of planning
		plannings = new ArrayList<Planning>();
		
		persons = new ArrayList<Person>();
		
		// creation of lists of participants
		Person p1 = new Person();p1.setUid("11008880");persons.add(p1);
		Person p2 = new Person();p2.setUid("10367894");persons.add(p2);
		Person p3 = new Person();p3.setUid("12005689");persons.add(p3);
		Person p4 = new Person();p4.setUid("foursovM");persons.add(p4);
		Person p5 = new Person();p5.setUid("certainD");persons.add(p5);
		Person p6 = new Person();p6.setUid("grossamblardD");persons.add(p6);
		
		for(Person p : persons){
			personDAO.persist(p);
		}
		
		Participant pa1 = new Participant();pa1.setFollowingTeacher(p5);pa1.setStudent(p1);
		Participant pa2 = new Participant();pa2.setFollowingTeacher(p5);pa2.setStudent(p2);
		Participant pa3 = new Participant();pa3.setFollowingTeacher(p4);pa3.setStudent(p3);
		Participant pa4 = new Participant();pa4.setFollowingTeacher(p4);pa4.setStudent(p1);
		Participant pa5 = new Participant();pa5.setFollowingTeacher(p5);pa5.setStudent(p1);
		Participant pa6 = new Participant();pa6.setFollowingTeacher(p5);pa6.setStudent(p2);
		
		Collection<Participant> participants1 = Lists.newArrayList(pa1, pa2, pa3);
		Collection<Participant> participants2 = Lists.newArrayList(pa4, pa5, pa6);
		
		// creation of priorities
		Priority priority = new Priority();
		priority.setWeight(5);
		
		Collection<Priority> priorities = Lists.newArrayList();
		priorities.add(priority);
		
		// creation list of plannings
		Planning pl1 = new Planning();pl1.setParticipants(participants1);pl1.setAdmin(p5);
		pl1.setPriorities(priorities);
		Planning pl2 = new Planning();pl2.setParticipants(participants2);pl2.setAdmin(p6);pl2.setName("CestLePlanningDeGrosAmblard");
		plannings.add(pl1);
		plannings.add(pl2);
		
		for(Planning p : plannings){
			planningDAO.persist(p);
		}
		
	}
	
	@After
	public void after(){
		// removing of all planning
		planningDAO.deleteAll();
		participantDAO.deleteAll();
		personDAO.deleteAll();
		assertEquals(planningDAO.findAll().size(),0);
	}
	
	@Test 
	public void testFindAllByUid(){
		List<Planning> plannings = planningDAO.findAll("11008880");
		
		assertEquals(plannings.size(), 2);
		
		plannings = planningDAO.findAll("grossamblardD");
		
		assertEquals(plannings.size(), 1);
		
		assertEquals(plannings.get(0).getName(), "CestLePlanningDeGrosAmblard");
		
	}
	
	@Test
	public void testFindAll() {
		// we test if the number of retrieved planning is the same that the added ones
		List<Planning> retrievedPlannings = planningDAO.findAll();
		assertTrue(retrievedPlannings.size() == plannings.size());		
		
	}
	
	@Test
	public void testFindById(){
		Planning p = plannings.get(0);
		assertTrue(p.getId() != null);
		
		Planning retrievedPlanning = planningDAO.findById(p.getId());
		assertEquals(retrievedPlanning.getId(), p.getId());
	}
	
	@Test
	public void testFindParticipants(){
		Planning p = plannings.get(0);
		assertTrue(p.getId() != null);
		
		Planning retrievedPlanning = planningDAO.findById(p.getId());
		List<Participant> participants = (List<Participant>) planningDAO.findParticipants(retrievedPlanning);
		assertEquals(3, participants.size());
	}

	@Test
	public void testFindPriorities(){
		Planning p = plannings.get(0);
		assertTrue(p.getId() != null);
		
		Planning retrievedPlanning = planningDAO.findById(p.getId());
		List<Priority> priorities = (List<Priority>) planningDAO.findPriorities(retrievedPlanning);
		assertTrue(priorities.size() > 0);
		Priority priority = priorities.get(0);
		assertTrue(priority.getWeight() == 5);
	}
}
