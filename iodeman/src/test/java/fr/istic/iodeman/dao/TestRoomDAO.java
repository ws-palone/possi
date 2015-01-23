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

public class TestRoomDAO extends AbstractSpringUnitTest{

	@Autowired
	RoomDAO roomDAO;

	List<Room> rooms;
	
	@Before
	public void setUp(){
		// removing of all planning
		roomDAO.deleteAll();
		
		// creation of a list of planning
		rooms = new ArrayList<Room>();
		
		for(int i = 0; i<3; i++){
			// creation de la room
			Room room = new Room();
			room.setName("i5"+i);
			// on l'ajoute
			rooms.add(room);
		}
		
		assertTrue(rooms.size() == 3);
		
		// adding in the database
		for(Room r : rooms){
			roomDAO.persist(r);
		}
		
	}
	
	@After
	public void after(){
		// removing of all the plannings created
		for(Room o : rooms){
			roomDAO.delete(o);
		}
		assertEquals(roomDAO.findAll().size(),0);		
	}
	
	
	@Test
	public void testFindAll() {
		// we test if the number of retrieved oraldefense is the same that the added ones
		List<Room> retrievedRooms = roomDAO.findAll();
		assertTrue(retrievedRooms.size() == rooms.size());		
		
	}
	
	@Test
	public void testFindById(){
		Room r = rooms.get(0);
		assertTrue(r != null);
		
		Room retrievedRoom = roomDAO.findById(r.getId());
		assertEquals(retrievedRoom.getId(), r.getId());
	}

}
