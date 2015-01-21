package fr.istic.iodeman.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.istic.iodeman.Application;
import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.utils.AbstractSpringUnitTest;

public class TestPlanningDao extends AbstractSpringUnitTest {

	PlanningDAO planningDAO;

	List<Planning> plannings;
	
	@Before
	public void setUp(){
		// instanciate the DAO
		planningDAO = new PlanningDAOImpl();
		
		// removing of all planning
		planningDAO.deleteAll();
		
		// creation of a list of planning
		plannings = new ArrayList<Planning>();
		
		for(int i = 0; i<3; i++){
			Planning p = new Planning();
			p.setName("Planning "+i);
			plannings.add(p);
		}
		
		assertTrue(plannings.size() == 3);
		
		// adding in the database
		for(Planning p : plannings){
			planningDAO.persist(p);
		}
		
	}
	
	@After
	public void after(){
		// removing of all the plannings created
		for(Planning p : plannings){
			planningDAO.delete(p);
		}
		assertEquals(planningDAO.findAll().size(),0);		
	}
	
	
	@Test
	public void testFindAll() {
		// we test if the number of retrieved planning is the same that the added ones
		List<Planning> retrievedPlannings = planningDAO.findAll();
		assertTrue(retrievedPlannings.size() == plannings.size());		
		
	}

}
