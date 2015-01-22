package fr.istic.iodeman.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.istic.iodeman.model.Participant;
import fr.istic.iodeman.model.Person;
import fr.istic.iodeman.utils.AbstractSpringUnitTest;

public class TestParticipantDAO extends AbstractSpringUnitTest {

	@Autowired
	ParticipantDAO participantDAO;

	List<Participant> participants;
	
	@Before
	public void setUp(){
		// removing of all planning
		participantDAO.deleteAll();
		
		// creation of a list of planning
		participants = new ArrayList<Participant>();
		
		for(int i = 0; i<3; i++){
			Participant p = new Participant();
			
			// creation of an student
			Person student = new Person();
			student.setFirstName("Dummy"+i);
			
			p.setStudent(student);
			participants.add(p);
		}
		
		assertTrue(participants.size() == 3);
		
		// adding in the database
		for(Participant p : participants){
			participantDAO.persist(p);
		}
		
	}
	
	@After
	public void after(){
		// removing of all the plannings created
		for(Participant p : participants){
			participantDAO.delete(p);
		}
		assertEquals(participantDAO.findAll().size(),0);		
	}
	
	
	@Test
	public void testFindAll() {
		// we test if the number of retrieved planning is the same that the added ones
		List<Participant> retrievedParticipants = participantDAO.findAll();
		assertTrue(retrievedParticipants.size() == participants.size());		
		
	}
	
	@Test
	public void testFindById(){
		Participant p = participants.get(0);
		assertTrue(p.getId() != null);
		
		Participant retrievedParticipant = participantDAO.findById(p.getId());
		assertEquals(retrievedParticipant.getId(), p.getId());
	}

}
