package fr.istic.iodeman.service;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fr.istic.iodeman.dao.RoomDAO;
import fr.istic.iodeman.model.Room;

public class TestRoomService {

	@InjectMocks
	private RoomService roomService;

	@Mock
	private RoomDAO roomDAO;

	@Before
	public void setUp() {

		roomService = new RoomServiceImpl();
		MockitoAnnotations.initMocks(this);

	}

	@Test
	public void testCreate() {
		String nameRoom= "i50";

		roomService.findOrCreate(nameRoom);

		ArgumentCaptor<Room> argument = ArgumentCaptor.forClass(Room.class);
		Mockito.verify(roomDAO).persist(argument.capture());

		Room r = argument.getValue();

		assertEquals(nameRoom, r.getName());

	}
	
	@Test
	public void testFind() {
		
		Room room = new Room();
		room.setId(1);
		room.setName("i50");
		
		Mockito.when(roomDAO.findByName(room.getName())).thenReturn(room);
		
		Room result = roomService.findOrCreate(room.getName());
		
		assertEquals(room, result);
		
	}

	@Test
	public void testFindAll() {

		roomService.findAll();
		Mockito.verify(roomDAO).findAll();

	}
}
