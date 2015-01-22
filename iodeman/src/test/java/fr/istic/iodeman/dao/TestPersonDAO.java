package fr.istic.iodeman.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.istic.iodeman.model.Person;
import fr.istic.iodeman.utils.AbstractSpringUnitTest;

public class TestPersonDAO extends AbstractSpringUnitTest {

	@Autowired
	PersonDAO personDAO;

	List<Person> persons;
	
	@Before
	public void setUp(){		
		// removing of all planning
		personDAO.deleteAll();
		
		// creation of a list of planning
		persons = new ArrayList<Person>();
		
		for(int i = 0; i<3; i++){
			Person p = new Person();
			p.setFirstName("Dummy");
			persons.add(p);
		}
		
		assertTrue(persons.size() == 3);
		
		// adding in the database
		for(Person p : persons){
			personDAO.persist(p);
		}
		
	}
	
	@After
	public void after(){
		// removing of all the persons created
		for(Person p : persons){
			personDAO.delete(p);
		}
		assertEquals(personDAO.findAll().size(),0);		
	}
	
	
	@Test
	public void testFindAll() {
		// we test if the number of retrieved person is the same that the added ones
		List<Person> retrievedPersons = personDAO.findAll();
		assertTrue(retrievedPersons.size() == persons.size());		
		
	}
	
	@Test
	public void testFindById(){
		Person p = persons.get(0);
		assertTrue(p.getId() != null);
		
		Person retrievedPerson = personDAO.findById(p.getId());
		assertEquals(retrievedPerson.getId(), p.getId());
	}

}
