package fr.istic.iodeman.controllers;

import fr.istic.iodeman.models.Room;
import fr.istic.iodeman.services.RoomService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/rooms")
@RestController
public class RoomController {

	private final RoomService roomService;

	public RoomController(RoomService roomService) {
		this.roomService = roomService;
	}

	@RequestMapping
	public Iterable<Room> listAll(){
		return roomService.findAll ();
	}


	@DeleteMapping("/{id}")
	public Room delete(@PathVariable("id") Long id) {
		// fixme verifier si c'est un admin
		//session.teacherOnly();
		return roomService.delete(id);
	}

	@PostMapping
	public  List<Room> create(@RequestBody List<String> names){
		return roomService.findOrCreateManyRooms(names);
	}


}
