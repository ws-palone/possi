package fr.istic.iodeman.services;

import fr.istic.iodeman.models.Planning;
import fr.istic.iodeman.models.Room;
import fr.istic.iodeman.repositories.PlanningRepository;
import fr.istic.iodeman.repositories.RoomRepository;
import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

	private final RoomRepository roomRepository;
	
	private final PlanningRepository planningRepository;

	public RoomServiceImpl(RoomRepository roomRepository, PlanningRepository planningRepository) {
		this.roomRepository = roomRepository;
		this.planningRepository = planningRepository;
	}

	@Override
	public List<Room> findOrCreateManyRooms(List<String> names) {
		List<Room>rooms = new ArrayList<>();
		for (String name : names) {
			Room room = roomRepository.findByName(name);
			if (room == null) {
				room = new Room();
				room.setName(name);;
				rooms.add(roomRepository.save(room));
			}
		}
		return  rooms;
	}

	@Override
	public Iterable<Room> findAll() {
		return roomRepository.findAll();
	}

	@Override
	public Room delete(Long roomId) {
		boolean isNotInPlanning = true;

		Room room = roomRepository.findById(roomId).get();
		Validate.notNull(room);
		
		Iterable<Planning> plannings = planningRepository.findAll();
		
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
			roomRepository.delete(room);
			return room;
		}
		
		return null;
	}

}
