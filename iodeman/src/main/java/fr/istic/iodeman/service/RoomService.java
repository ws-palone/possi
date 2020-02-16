package fr.istic.iodeman.service;

import fr.istic.iodeman.model.Room;

import java.util.List;

public interface RoomService {

	public Room findOrCreate(String name);
	// FIXME: 16/02/2020 créer des rooms en partant d'une liste
	public List<Room> findOrCreateManyRooms(List<String> names);
	
	public List<Room> findAll();
	
	public Room delete(int roomID);
	
}
