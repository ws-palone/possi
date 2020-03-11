package fr.istic.iodeman.controller;

import fr.istic.iodeman.SessionComponent;
import fr.istic.iodeman.model.Room;
import fr.istic.iodeman.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLOutput;
import java.util.List;

@RequestMapping("/room")
@RestController
public class RoomController {

	@Autowired
	private RoomService roomService;

	//@Autowired
	//private SessionComponent session;

	@RequestMapping("/list")
	public List<Room> listAll(){
		return roomService.findAll();
	}

	@RequestMapping("/create")
	public Room create(@RequestParam("name") String name) {
		// FIXME: 16/02/2020 a decomenter
		//session.teacherOnly();
		return roomService.findOrCreate(name);
	}

	@RequestMapping("/delete")
	public Room create(@RequestParam("id") int id) {
		// FIXME: 16/02/2020 a décommenter
		//session.teacherOnly();
		return roomService.delete(id);
	}

	// FIXME: 16/02/2020 créer des rooms en partant d'une liste
	@RequestMapping(value = "/createMany", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public  List<Room> create(@RequestBody List<String> names){
		return roomService.findOrCreateManyRooms(names);
	}


}
