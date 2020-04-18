package fr.istic.iodeman.services;


import fr.istic.iodeman.models.Room;

import java.util.List;

public interface RoomService {

	List<Room> findOrCreateManyRooms(List<String> names);

	Iterable<Room> findAll();

	Room delete(Long roomId);
}
