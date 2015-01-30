package fr.istic.iodeman.service;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.istic.iodeman.dao.PersonDAO;
import fr.istic.iodeman.dao.PlanningDAO;
import fr.istic.iodeman.model.Participant;
import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.Room;
import fr.istic.iodeman.model.TimeBox;
import fr.istic.iodeman.resolver.PersonMailResolver;
import fr.istic.iodeman.strategy.ParticipantsExcelImport;
import fr.istic.iodeman.strategy.ParticipantsImport;

@Service
public class PlanningServiceImpl implements PlanningService {

	@Autowired
	private PlanningDAO planningDAO;
	
	@Autowired
	private PersonMailResolver personResolver;
	
	@Autowired
	private PersonDAO personDAO;
	
	public List<Planning> findAll() {
		return planningDAO.findAll();
	}

	public Planning findById(Integer id) {
		return planningDAO.findById(id);
	}
	
	public Planning create(String name, TimeBox period, Integer oralDefenseDuration, 
			Integer oralDefenseInterlude, TimeBox lunchBreak,
			TimeBox dayPeriod, Integer nbMaxOralDefensePerDay,
			Collection<Room> rooms) {
		
		Validate.notEmpty(name);
		Validate.notNull(period);
		Validate.notNull(oralDefenseDuration);
		Validate.isTrue(oralDefenseDuration > 0);
		Validate.notNull(dayPeriod);
		
		period.validate();
		dayPeriod.validate();
		if (lunchBreak != null) {
			lunchBreak.validate();
		}
		
		Planning planning = new Planning();
		planning.setName(name);
		planning.setPeriod(period);
		planning.setOralDefenseDuration(oralDefenseDuration);
		planning.setOralDefenseInterlude(oralDefenseInterlude);
		planning.setLunchBreak(lunchBreak);
		planning.setDayPeriod(dayPeriod);
		planning.setNbMaxOralDefensePerDay(nbMaxOralDefensePerDay);
		planning.setRooms(rooms);
		
		planningDAO.persist(planning);
		
		return planning;
	}
	
	public void update(Planning planning, String name, TimeBox period,
			Integer oralDefenseDuration, Integer oralDefenseInterlude,
			TimeBox lunchBreak, TimeBox dayPeriod,
			Integer nbMaxOralDefensePerDay, Collection<Room> rooms) {
		
		Validate.notNull(planning);
		
		if (name != null && !name.equals("")) {
			planning.setName(name);
		}
		
		if (period != null) {
			period.validate();
			planning.setPeriod(period);
		}
		
		if (oralDefenseDuration != null && oralDefenseDuration > 0) {
			planning.setOralDefenseDuration(oralDefenseDuration);
		}
		
		if (dayPeriod != null) {
			dayPeriod.validate();
			planning.setDayPeriod(period);
		}
		
		if (lunchBreak != null) {
			lunchBreak.validate();
			planning.setLunchBreak(lunchBreak);
		}
		
		planning.setOralDefenseInterlude(oralDefenseInterlude);
		planning.setNbMaxOralDefensePerDay(nbMaxOralDefensePerDay);
		
		if (rooms != null) {
			planning.setRooms(rooms);
		}
		
		planningDAO.persist(planning);
		
	}
	
	public Planning importPartcipants(Planning planning, File file) throws Exception {
		
		ParticipantsImport participantsImport = new ParticipantsExcelImport();
		participantsImport.configure(personResolver);
		Collection<Participant> participants = participantsImport.execute(file);
		
		planning.setParticipants(participants);
		
		planningDAO.persist(planning);
		
		return planning;
	}


}
