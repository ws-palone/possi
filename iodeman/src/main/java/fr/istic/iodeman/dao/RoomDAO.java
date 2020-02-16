package fr.istic.iodeman.dao;

import fr.istic.iodeman.model.Room;

import java.util.List;

public interface RoomDAO {
	public void persist(Room r);
	// FIXME: 16/02/2020 créer des rooms en partant d'une liste
	public void persit(List<String> names);
	
	public Room findById(int id);
	
	public Room findByName(String name);

	public void delete(Room r) ;

	public List<Room> findAll();

	public void deleteAll();
	
}
