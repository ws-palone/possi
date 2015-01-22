package fr.istic.iodeman.dao;

import java.util.List;

import fr.istic.iodeman.model.Room;

public interface RoomDAO {
	public void persist(Room r);
	
	public Room findById(int id);

	public void delete(Room r) ;

	public List<Room> findAll();

	public void deleteAll();
	
}
