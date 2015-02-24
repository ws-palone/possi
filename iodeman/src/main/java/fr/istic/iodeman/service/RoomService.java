package fr.istic.iodeman.service;

import java.util.List;

import fr.istic.iodeman.model.Room;

public interface RoomService {

	public Room findOrCreate(String name);
	
	public List<Room> findAll();
	
	public Room delete(int roomID);
	
}
