package fr.istic.iodeman.service;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;

import fr.istic.iodeman.dao.RoomDAO;
import fr.istic.iodeman.model.Planning;
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
	public void testFindOrCreate() {
		String nameRoom= "i50";

		roomService.findOrCreate(nameRoom);

		ArgumentCaptor<Room> argument = ArgumentCaptor.forClass(Room.class);
		Mockito.verify(roomDAO).persist(argument.capture());

		Room r = argument.getValue();

		assertEquals(nameRoom, r.getName());

	}

	@Test
	public void testFindAll() {

		roomService.findAll();
		Mockito.verify(roomDAO).findAll();

	}
}
