package fr.istic.iodeman.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.istic.iodeman.model.Priority;
import fr.istic.iodeman.utils.AbstractSpringUnitTest;

public class TestPriorityDAO extends AbstractSpringUnitTest {

	@Autowired
	PriorityDAO priorityDAO;

	List<Priority> priorities;
	
	@Before
	public void setUp(){		
		// removing of all priorities
		priorityDAO.deleteAll();
		
		// creation of a list of priority
		priorities = new ArrayList<Priority>();
		
		for(int i = 0; i<3; i++){
			Priority p = new Priority();
			p.setWeight(i);
			priorities.add(p);
		}
		
		assertTrue(priorities.size() == 3);
		
		// adding in the database
		for(Priority p : priorities){
			priorityDAO.persist(p);
		}
		
	}
	
	@After
	public void after(){
		// removing of all the priorities created
		for(Priority p : priorities){
			priorityDAO.delete(p);
		}
		assertEquals(priorityDAO.findAll().size(),0);		
	}
	
	
	@Test
	public void testFindAll() {
		// we test if the number of retrieved planning is the same that the added ones
		List<Priority> retrievedPriorities = priorityDAO.findAll();
		assertTrue(retrievedPriorities.size() == priorities.size());		
		
	}
	
	@Test
	public void testFindById(){
		Priority p = priorities.get(0);
		assertTrue(p.getId() != null);
		
		Priority retrievedPriorities = priorityDAO.findById(p.getId());
		assertEquals(retrievedPriorities.getId(), p.getId());
	}

}
