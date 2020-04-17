package fr.istic.iodeman.services;

import fr.istic.iodeman.model.Room;

import java.util.List;

public interface RoomService {

	List<Room> findOrCreateManyRooms(List<String> names);
	
	List<Room> findAll();
	
	Room delete(int roomID);
	
}
