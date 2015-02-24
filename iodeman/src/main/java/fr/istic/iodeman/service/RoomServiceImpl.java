package fr.istic.iodeman.service;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.istic.iodeman.dao.PlanningDAO;
import fr.istic.iodeman.dao.RoomDAO;
import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.Room;

@Service
public class RoomServiceImpl implements RoomService {

	@Autowired
	private RoomDAO roomDAO;
	
	@Autowired
	private PlanningDAO planningDAO;

	public Room findOrCreate(String name) {

		Room room = roomDAO.findByName(name);

		if (room == null) {

			room = new Room();
			room.setName(name);

			roomDAO.persist(room);
		}

		return room;

	}

	@Override
	public List<Room> findAll() {
		return roomDAO.findAll();
	}

	@Override
	public Room delete(int roomID) {
		boolean isNotInPlanning = true;

		Room room = roomDAO.findById(roomID);
		Validate.notNull(room);
		
		List<Planning> plannings = planningDAO.findAll();
		
		System.out.println(room.getName());
		
		for(Planning p : plannings){
			Collection<Room> rooms = p.getRooms();
			
			for(Room r : rooms){
				if(room.getId() == r.getId()){
					isNotInPlanning = false;
				}
			}
		}

		if(isNotInPlanning){
			roomDAO.delete(room);
			return room;
		}
		
		return null;
	}

}
