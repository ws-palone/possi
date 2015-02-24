package fr.istic.iodeman.service;

import java.io.File;
import java.util.Collection;
import java.util.List;

import fr.istic.iodeman.dto.ParticipantDTO;
import fr.istic.iodeman.model.OralDefense;
import fr.istic.iodeman.model.Participant;
import fr.istic.iodeman.model.Person;
import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.Priority;
import fr.istic.iodeman.model.Room;
import fr.istic.iodeman.model.TimeBox;

public interface PlanningService {

	public List<Planning> findAll();
	
	public Planning findById(Integer id);
	
	public Planning create(Person admin, String name, TimeBox period, Integer oralDefenseDuration, 
			Integer oralDefenseInterlude, TimeBox lunchBreak,
			TimeBox dayPeriod, Integer nbMaxOralDefensePerDay,
			Collection<Room> rooms);
	
	public void update(Planning planning, String name, TimeBox period, Integer oralDefenseDuration, 
			Integer oralDefenseInterlude, TimeBox lunchBreak,
			TimeBox dayPeriod, Integer nbMaxOralDefensePerDay,
			Collection<Room> rooms);
	
	public Planning importPartcipants(Planning planning, File file) throws Exception;
	
	public Collection<Participant> findParticipants(Planning planning);
	
	public Collection<Priority> findPriorities(Planning planning);
	
	public Collection<Priority> updatePriorities(Planning planning, Collection<Priority> priorities);
	
	public File exportExcel(Planning planning);
	
	public Collection<OralDefense> export(Integer planningId);
	
	public void validate(Planning planning);

	public List<Planning> findAllByUid(String uid);
	
	public Collection<ParticipantDTO> findParticipantsAndUnavailabilitiesNumber(Planning planning);
	
	public void delete(Planning planning);
}
