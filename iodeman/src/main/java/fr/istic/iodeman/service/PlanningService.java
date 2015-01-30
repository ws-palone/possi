package fr.istic.iodeman.service;

import java.io.File;
import java.util.Collection;
import java.util.List;

import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.Room;
import fr.istic.iodeman.model.TimeBox;

public interface PlanningService {

	public List<Planning> findAll();
	
	public Planning findById(Integer id);
	
	public Planning create(String name, TimeBox period, Integer oralDefenseDuration, 
			Integer oralDefenseInterlude, TimeBox lunchBreak,
			TimeBox dayPeriod, Integer nbMaxOralDefensePerDay,
			Collection<Room> rooms);
	
	public Planning importPartcipants(Planning planning, File file) throws Exception;
	
}
