package fr.istic.iodeman.service;

import fr.istic.iodeman.model.Room;

public interface RoomService {

	public Room findOrCreate(String name);
	
}
