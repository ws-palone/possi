package fr.istic.iodeman.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.istic.iodeman.SessionComponent;
import fr.istic.iodeman.model.Room;
import fr.istic.iodeman.service.RoomService;

@RequestMapping("/room")
@RestController
public class RoomController {
	
	@Autowired
	private RoomService roomService;
	
	@Autowired
	private SessionComponent session;
	
	@RequestMapping("/list")
	public List<Room> listAll(){
		
		return roomService.findAll();
	}
	
	@RequestMapping("/create")
	public Room create(@RequestParam("name") String name) {
		session.teacherOnly();
		return roomService.findOrCreate(name);
	}
	
	@RequestMapping("/delete")
	public Room create(@RequestParam("id") int id) {
		session.teacherOnly();
		return roomService.delete(id);
	}

}
