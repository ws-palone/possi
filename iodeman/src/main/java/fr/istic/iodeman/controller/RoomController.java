package fr.istic.iodeman.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.istic.iodeman.model.Room;
import fr.istic.iodeman.service.RoomService;

@RequestMapping("/room")
@RestController
public class RoomController {
	
	@Autowired
	private RoomService roomService;
	
	@RequestMapping("/list")
	public List<Room> listAll(){
		
		return roomService.findAll();
	}
	
	@RequestMapping("/create")
	public Room create(@RequestParam("name") String name) {
		
		return roomService.findOrCreate(name);
	}

}
