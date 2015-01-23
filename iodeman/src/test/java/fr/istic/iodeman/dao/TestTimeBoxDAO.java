package fr.istic.iodeman.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.istic.iodeman.model.TimeBox;
import fr.istic.iodeman.utils.AbstractSpringUnitTest;

public class TestTimeBoxDAO extends AbstractSpringUnitTest{

	@Autowired
	TimeBoxDAO timeBoxDAO;

	List<TimeBox> timeboxes;
	
	@Before
	public void setUp(){
		// removing of all planning
		timeBoxDAO.deleteAll();
		
		// creation of a list of planning
		timeboxes = new ArrayList<TimeBox>();
		
		for(int i = 0; i<3; i++){
			TimeBox o = new TimeBox();
			o.setFrom(new Date());
			// on ajoute la soutenance à la liste
			timeboxes.add(o);
		}
		
		assertTrue(timeboxes.size() == 3);
		
		// adding in the database
		for(TimeBox o : timeboxes){
			timeBoxDAO.persist(o);
		}
		
	}
	
	@After
	public void after(){
		// removing of all the plannings created
		for(TimeBox o : timeboxes){
			timeBoxDAO.delete(o);
		}
		assertEquals(timeBoxDAO.findAll().size(),0);		
	}
	
	
	@Test
	public void testFindAll() {
		// we test if the number of retrieved TimeBox is the same that the added ones
		List<TimeBox> retrievedTimeBoxs = timeBoxDAO.findAll();
		assertTrue(retrievedTimeBoxs.size() == timeboxes.size());		
		
	}
	
	@Test
	public void testFindById(){
		TimeBox o = timeboxes.get(0);
		assertTrue(o.getId() != null);
		
		TimeBox retrievedTimeBox = timeBoxDAO.findById(o.getId());
		assertEquals(retrievedTimeBox.getId(), o.getId());
	}

}
