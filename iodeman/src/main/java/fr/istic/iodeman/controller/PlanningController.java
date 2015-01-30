package fr.istic.iodeman.controller;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.Room;
import fr.istic.iodeman.model.TimeBox;
import fr.istic.iodeman.service.PlanningService;
import fr.istic.iodeman.service.RoomService;

@RequestMapping("/planning") 
@RestController
public class PlanningController {
	
	@Autowired
	private RoomService roomService;

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
			@RequestParam("rooms") List<String> rooms
			) {
		
		//URL_TEST : http://iode-man-debian.istic.univ-rennes1.fr:8080/iodeman/planning/create?name=toto&periodStart=2015-01-01&periodEnd=2015-01-07&oralDefenseDuration=60&oralDefenseInterlude=15&lunchBreakStart=12:15&lunchBreakEnd=14:00&dayPeriodStart=08:00&dayPeriodEnd=18:15&nbMaxOralDefensePerDay=6&rooms=i51
		
		Collection<Room> roomsCollection = Collections2.transform(rooms, new Function<String, Room>() {
			@Override
			public Room apply(String roomName) {
				return roomService.findOrCreate(roomName);
			}
		});
		
		TimeBox period = new TimeBox(periodStart, periodEnd);
		TimeBox lunch = new TimeBox(lunchBreakStart, lunchBreakEnd);
		TimeBox dayPeriod = new TimeBox(dayPeriodStart, dayPeriodEnd);
		
		return planningService.create(name, period, oralDefenseDuration, oralDefenseInterlude, lunch, dayPeriod, nbMaxOralDefensePerDay, roomsCollection);
	}
	
	@RequestMapping("/update")
	public Planning updatePlanning(
			@RequestParam("planningID") Integer planningID,
			@RequestParam(value="name", required=false) String name,
			@RequestParam(value="periodStart", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date periodStart,
			@RequestParam(value="periodEnd", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date periodEnd,
			@RequestParam(value="oralDefenseDuration", required=false) Integer oralDefenseDuration,
			@RequestParam(value="oralDefenseInterlude", required=false) Integer oralDefenseInterlude, 
			@RequestParam(value="lunchBreakStart", required=false) @DateTimeFormat(pattern="HH:mm") Date lunchBreakStart,
			@RequestParam(value="lunchBreakEnd", required=false) @DateTimeFormat(pattern="HH:mm") Date lunchBreakEnd, 
			@RequestParam(value="dayPeriodStart", required=false) @DateTimeFormat(pattern="HH:mm") Date dayPeriodStart,
			@RequestParam(value="dayPeriodEnd", required=false) @DateTimeFormat(pattern="HH:mm") Date dayPeriodEnd,
			@RequestParam(value="nbMaxOralDefensePerDay", required=false) Integer nbMaxOralDefensePerDay,
			@RequestParam(value="rooms", required=false) List<String> rooms
			) {
		
		Planning planning = planningService.findById(planningID);
		Validate.notNull(planning);
		
		Collection<Room> roomsCollection = null;
		
		if (rooms != null) {
			roomsCollection = Collections2.transform(rooms, new Function<String, Room>() {
				@Override
				public Room apply(String roomName) {
					return roomService.findOrCreate(roomName);
				}
			});
		}
		
		TimeBox period = (periodStart != null && periodEnd != null) ? new TimeBox(periodStart, periodEnd) : null;
		TimeBox lunch = (lunchBreakStart != null && lunchBreakEnd != null) ? new TimeBox(lunchBreakStart, lunchBreakEnd) : null;
		TimeBox dayPeriod = (dayPeriodStart != null && dayPeriodEnd != null) ? new TimeBox(dayPeriodStart, dayPeriodEnd) : null;
		
		planningService.update(planning, name, period, oralDefenseDuration, oralDefenseInterlude, lunch, dayPeriod, nbMaxOralDefensePerDay, roomsCollection);
		
		return planning;
	}
	
}
