package fr.istic.iodeman.service;

import fr.istic.iodeman.dao.PlanningDAO;
import fr.istic.iodeman.dao.RoomDAO;
import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.Room;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

	//private String[] names ={};
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
	// FIXME: 16/02/2020 créer des rooms en partant d'une liste
	public List<Room> findOrCreateManyRooms(List<String> names) {
		List<Room>rooms = new ArrayList<>();
		for (String name : names) {
			Room room = roomDAO.findByName(name);
			if (room == null) {
				room = new Room();
				room.setName(name);
				roomDAO.persist(room);
				rooms.add(room);
				System.out.println(room.getId());
			}
		}
		return  rooms;

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
