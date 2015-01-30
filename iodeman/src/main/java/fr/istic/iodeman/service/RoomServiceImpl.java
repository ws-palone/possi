package fr.istic.iodeman.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.istic.iodeman.dao.RoomDAO;
import fr.istic.iodeman.model.Room;

@Service
public class RoomServiceImpl implements RoomService {

	@Autowired
	private RoomDAO roomDAO;
	
	public Room findOrCreate(String name) {
		
		Room room = roomDAO.findByName(name);
		
		if (room == null) {
			
			room = new Room();
			room.setName(name);
			
			roomDAO.persist(room);
		}
		
		return room;
		
	}

}
