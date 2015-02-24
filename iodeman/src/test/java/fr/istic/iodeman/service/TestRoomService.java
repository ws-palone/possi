package fr.istic.iodeman.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;

import fr.istic.iodeman.dao.PlanningDAO;
import fr.istic.iodeman.dao.RoomDAO;
import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.Room;

public class TestRoomService {

	@InjectMocks
	private RoomService roomService;

	@Mock
	private RoomDAO roomDAO;
	
	@Mock
	private PlanningDAO planningDAO;

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
	
	@Test
	public void delete(){
		
		Room room = new Room();
		room.setId(1);
		room.setName("i50");
		
		List<Planning> plannings = Lists.newArrayList();
		
		Mockito.when(roomDAO.findByName(room.getName())).thenReturn(room);

		roomService.findOrCreate(room.getName());
		
		Mockito.when(roomDAO.findById(room.getId())).thenReturn(room);
		
		Mockito.when(planningDAO.findAll()).thenReturn(plannings);
		
		roomService.delete(room.getId());
		
		ArgumentCaptor<Room> argument = ArgumentCaptor.forClass(Room.class);
		Mockito.verify(roomDAO).delete(argument.capture());

		Room r = argument.getValue();

		assertEquals(room, r);
	}
}
