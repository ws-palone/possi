package fr.istic.iodeman.controller;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.Room;
import fr.istic.iodeman.model.TimeBox;
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
	public Planning createPlanning(
			@RequestParam("name") String name,
			@RequestParam("periodStart") @DateTimeFormat(pattern="yyyy-MM-dd") Date periodStart,
			@RequestParam("periodEnd") @DateTimeFormat(pattern="yyyy-MM-dd") Date periodEnd,
			@RequestParam("oralDefenseDuration") Integer oralDefenseDuration,
			@RequestParam("oralDefenseInterlude") Integer oralDefenseInterlude, 
			@RequestParam("lunchBreakStart") @DateTimeFormat(pattern="HH:mm") Date lunchBreakStart,
			@RequestParam("lunchBreakEnd") @DateTimeFormat(pattern="HH:mm") Date lunchBreakEnd, 
			@RequestParam("dayPeriodStart") @DateTimeFormat(pattern="HH:mm") Date dayPeriodStart,
			@RequestParam("dayPeriodEnd") @DateTimeFormat(pattern="HH:mm") Date dayPeriodEnd,
			@RequestParam("nbMaxOralDefensePerDay") Integer nbMaxOralDefensePerDay,
			@RequestParam("rooms") Collection<Room> rooms
			) {
		
		TimeBox period = new TimeBox(periodStart, periodEnd);
		TimeBox lunch = new TimeBox(lunchBreakStart, lunchBreakEnd);
		TimeBox dayPeriod = new TimeBox(dayPeriodStart, dayPeriodEnd);
		
		return planningService.create(name, period, oralDefenseDuration, oralDefenseInterlude, lunch, dayPeriod, nbMaxOralDefensePerDay, rooms);
	}
	
}
