package fr.istic.iodeman.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.istic.iodeman.model.Unavailability;
import fr.istic.iodeman.utils.AbstractSpringUnitTest;

public class TestUnavailabilityDAO extends AbstractSpringUnitTest {

	@Autowired
	UnavailabilityDAO unavailabilityDAO;

	List<Unavailability> unavailabilities;
	
	@Before
	public void setUp(){		
		// removing of all planning
		unavailabilityDAO.deleteAll();
		
		// creation of a list of planning
		unavailabilities = new ArrayList<Unavailability>();
		
		for(int i = 0; i<3; i++){
			Unavailability p = new Unavailability();
			unavailabilities.add(p);
		}
		
		assertTrue(unavailabilities.size() == 3);
		
		// adding in the database
		for(Unavailability p : unavailabilities){
			unavailabilityDAO.persist(p);
		}
		
	}
	
	@After
	public void after(){
		// removing of all the Unavailabilitys created
		for(Unavailability p : unavailabilities){
			unavailabilityDAO.delete(p);
		}
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

}
