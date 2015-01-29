package fr.istic.iodeman.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import fr.istic.iodeman.model.OralDefense;
import fr.istic.iodeman.model.Room;
import fr.istic.iodeman.utils.AbstractSpringUnitTest;

public class TestOralDefenseDAO extends AbstractSpringUnitTest{

	@Autowired
	OralDefenseDAO oralDefenseDAO;

	List<OralDefense> oralDefenses;
	
	@Before
	public void setUp(){
		// removing of all planning
		oralDefenseDAO.deleteAll();
		
		// creation of a list of planning
		oralDefenses = new ArrayList<OralDefense>();
		
		for(int i = 0; i<3; i++){
			OralDefense o = new OralDefense();
			// creation de la room
//			Room room = new Room();
//			room.setName("i5"+i);
			// on l'ajoute
//			o.setRoom(room);
			// on ajoute la soutenance à la liste
			oralDefenses.add(o);
		}
		
		assertTrue(oralDefenses.size() == 3);
		
		// adding in the database
		for(OralDefense o : oralDefenses){
			oralDefenseDAO.persist(o);
		}
		
	}
	
	@After
	public void after(){
		// removing of all the plannings created
		for(OralDefense o : oralDefenses){
			oralDefenseDAO.delete(o);
		}
		assertEquals(oralDefenseDAO.findAll().size(),0);		
	}
	
	
	@Test
	public void testFindAll() {
		// we test if the number of retrieved oraldefense is the same that the added ones
		List<OralDefense> retrievedOralDefenses = oralDefenseDAO.findAll();
		assertTrue(retrievedOralDefenses.size() == oralDefenses.size());		
		
	}
	
	@Test
	public void testFindById(){
		OralDefense o = oralDefenses.get(0);
		assertTrue(o.getId() != null);
		
		OralDefense retrievedOralDefense = oralDefenseDAO.findById(o.getId());
		assertEquals(retrievedOralDefense.getId(), o.getId());
	}

}
