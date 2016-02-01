package fr.istic.iodeman.service;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import fr.istic.iodeman.builder.PlanningExportBuilder;
import fr.istic.iodeman.dao.ParticipantDAO;
import fr.istic.iodeman.dao.PersonDAO;
import fr.istic.iodeman.dao.PlanningDAO;
import fr.istic.iodeman.dao.PriorityDAO;
import fr.istic.iodeman.dao.UnavailabilityDAO;
import fr.istic.iodeman.dto.ParticipantDTO;
import fr.istic.iodeman.dto.PersonDTO;
import fr.istic.iodeman.model.OralDefense;
import fr.istic.iodeman.model.Participant;
import fr.istic.iodeman.model.Person;
import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.Priority;
import fr.istic.iodeman.model.Role;
import fr.istic.iodeman.model.Room;
import fr.istic.iodeman.model.TimeBox;
import fr.istic.iodeman.model.Unavailability;
import fr.istic.iodeman.resolver.PersonMailResolver;
import fr.istic.iodeman.strategy.ParticipantsCSVImport;
import fr.istic.iodeman.strategy.ParticipantsExcelImport;
import fr.istic.iodeman.strategy.ParticipantsImport;

@Service
public class PlanningServiceImpl implements PlanningService {

	@Autowired
	private PlanningDAO planningDAO;
	
	@Autowired
	private ParticipantDAO participantDAO;
	
	@Autowired
	private PriorityDAO priorityDAO;
	
	@Autowired
	private PersonMailResolver personResolver;
	
	@Autowired
	private PersonDAO personDAO;
	
	@Autowired
	private UnavailabilityDAO unavailabilityDAO;
	
	public List<Planning> findAll() {
		return planningDAO.findAll();
	}

	public Planning findById(Integer id) {
		return planningDAO.findById(id);
	}
	
	public Planning create(Person admin, String name, TimeBox period, Integer oralDefenseDuration, 
			Integer oralDefenseInterlude, TimeBox lunchBreak,
			TimeBox dayPeriod, Integer nbMaxOralDefensePerDay,
			Collection<Room> rooms) {
		
		Validate.notNull(admin);
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
		planning.setAdmin(admin);
		planning.setName(name);
		planning.setPeriod(period);
		planning.setOralDefenseDuration(oralDefenseDuration);
		planning.setOralDefenseInterlude(oralDefenseInterlude);
		planning.setLunchBreak(lunchBreak);
		planning.setDayPeriod(dayPeriod);
		planning.setNbMaxOralDefensePerDay(nbMaxOralDefensePerDay);
		planning.setRooms(rooms);
		
		planning.setPriorities(Lists.newArrayList(
				new Priority(Role.STUDENT, 1),
				new Priority(Role.PROF, 1)
		));
		
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
			planning.setDayPeriod(dayPeriod);
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
		
		planningDAO.update(planning);
		
	}
	
	public List<Planning> findAllByUid(String uid){
		return planningDAO.findAll(uid);
	}
	
	public Planning importPartcipants(Planning planning, File file) throws Exception {
		
		ParticipantsImport participantsImport = new ParticipantsCSVImport();
		participantsImport.configure(personResolver);
		Collection<Participant> participants = participantsImport.execute(file);
		
		for(Participant p : participants){
			participantDAO.persist(p);
		}
		
		planning.setParticipants(participants);
		
		planningDAO.update(planning);
		
		return planning;
	}
	
	public Collection<Participant> findParticipants(Planning planning) {
		
		return planningDAO.findParticipants(planning);
	}

	@Override
	public Collection<Priority> findPriorities(Planning planning) {
		return planningDAO.findPriorities(planning);
	}

	@Override
	public Collection<Priority> updatePriorities(Planning planning,
			Collection<Priority> priorities) {
		
		for(final Priority priority : priorities) {
			Collection<Priority> result = Collections2.filter(planning.getPriorities(), new Predicate<Priority>() {
				@Override
				public boolean apply(Priority p) {
					return p.getId().equals(priority.getId());
				}
			});
			if (result != null && result.size() > 0) {
				Priority p = Lists.newArrayList(result).get(0);
				p.setWeight(priority.getWeight());
				priorityDAO.update(priority);
			}
		}
		
		return planning.getPriorities();
	}

	@Override
	public File exportExcel(Planning planning) {		
		
		// verify the planning is not null
		Validate.notNull(planning);
		
		// retrieving of the unavailabilities
		Collection<Unavailability> unavailabilities = unavailabilityDAO.findByPlanningId(planning.getId());
				
		PlanningExportBuilder builder = new PlanningExportBuilder(planning);
		builder.setParticipants(planningDAO.findParticipants(planning));
		builder.setUnavailabilities(unavailabilities);
		
		File file = null;
		try {
			file = builder.split().build().toCSV();
			//file = builder.split().build().toExcel();
		} catch (Exception e) {
			System.out.println("Erreur de l'exportation lors de la fonction exportExcel: "+e.getMessage());
			e.printStackTrace();
		}
		
		//Validate.isTrue(file.exists());
		
		return file;
	}

	@Override
	public Collection<OralDefense> export(Integer planningId) {
		
		// retrieving the planning 
		Planning planning = planningDAO.findById(planningId);
		Validate.notNull(planning);
		
		// retrieving of the unavailabilities
		Collection<Unavailability> unavailabilities = unavailabilityDAO.findByPlanningId(planning.getId());
				
		// initialize builder
		PlanningExportBuilder builder = new PlanningExportBuilder(planning);
		builder.setParticipants(planningDAO.findParticipants(planning));
		builder.setUnavailabilities(unavailabilities);
		
		// build & return
		return builder.split().build().getOralDefenses();
		
	}

	@Override
	public void validate(Planning planning) {
		
		Validate.notNull(planning);
		
		// initialize builder
		PlanningExportBuilder builder = new PlanningExportBuilder(planning);
		builder.setParticipants(planningDAO.findParticipants(planning));
		
		builder.split().validate(); // throw exception if not validated
		
	}

	@Override
	public Collection<ParticipantDTO> findParticipantsAndUnavailabilitiesNumber(Planning planning) {
		// retrieving of participants
		Collection<Participant> participants = planningDAO.findParticipants(planning);
		
		// parsing for getting uids or person
		Collection<String> uids = Lists.newArrayList();
		for(Participant p : participants){
			// TODO replace with immutableSet
			if (!uids.contains(p.getStudent().getUid())) uids.add(p.getStudent().getUid());
			if (!uids.contains(p.getStudent().getUid())) uids.add(p.getFollowingTeacher().getUid());
		}
		
		// retrieved map<Person, Integer> from parsing result
		Map<String, Integer> map = (HashMap<String, Integer>)planningDAO.findParticipantsAndUnavailabilitiesNumber(planning, uids);
		
		// create collection of ParticipantDTO
		Collection<ParticipantDTO> participantsDTO = Lists.newArrayList();
		
		for(Participant p : participants){
			// DTO structure
			ParticipantDTO participantDTO = new ParticipantDTO();
			PersonDTO studentDTO = new PersonDTO();
			PersonDTO followingTeacherDTO = new PersonDTO();
			participantDTO.setStudent(studentDTO);
			participantDTO.setFollowingTeacher(followingTeacherDTO);
			participantDTO.setTutorFullName(p.getTutorFullName());
			participantDTO.setCompany(p.getCompany());
			participantsDTO.add(participantDTO);
			
			// student data 
			studentDTO.setPerson(p.getStudent());
			studentDTO.setUnavailabilitiesNumber(
					getUnavailabilitiesNumber(
							p.getStudent().getUid(),
							map
					)
			);
			
			// followingTeacher data
			followingTeacherDTO.setPerson(p.getFollowingTeacher());
			followingTeacherDTO.setUnavailabilitiesNumber(
					getUnavailabilitiesNumber(
							p.getFollowingTeacher().getUid(),
							map
					)
			);
		}
		
		// if the result is not null
		if (participantsDTO != null) return participantsDTO;
		
		return Lists.newArrayList();
	}
	
	private Integer getUnavailabilitiesNumber(String uid, Map<String, Integer> map){
		Integer nb = 0;
		if (map.containsKey(uid)){
			nb = map.get(uid);
		} 
		return nb;
	}

	@Override
	public void delete(Planning planning) {
		planningDAO.delete(planning);
	}
	
}
