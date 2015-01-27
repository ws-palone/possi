package fr.istic.iodeman.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.service.PlanningService;

@RequestMapping("/planning") 
@RestController
public class PlanningController {

	@Autowired
	private PlanningService planningService;
	
	@RequestMapping("/list")
	public List<Planning> listAll(){
		
		return planningService.findAll();
	}
	
	@RequestMapping("/create")
	public Planning createPlanning(@RequestParam("name") String name) {
		
		return planningService.create(name);
	}
	
}
