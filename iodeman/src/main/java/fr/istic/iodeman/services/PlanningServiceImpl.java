package fr.istic.iodeman.services;

import com.google.common.collect.Lists;
import fr.istic.iodeman.builder.PlanningExportBuilder;
import fr.istic.iodeman.dao.*;
import fr.istic.iodeman.dto.ParticipantDTO;
import fr.istic.iodeman.dto.PersonDTO;
import fr.istic.iodeman.model.*;
import fr.istic.iodeman.resolver.PersonMailResolver;
import fr.istic.iodeman.strategy.PlanningSplitter;
import fr.istic.iodeman.strategy.PlanningSplitterImpl;
import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PlanningServiceImpl implements PlanningService {

	private final PlanningDAO planningDAO;

	final
	ParticipantService participantService;

	final
	OralDefenseService oralDefenseService;

	private final PersonMailResolver personResolver;

	private final UnavailabilityDAO unavailabilityDAO;

	private final PlanningSplitter planningSplitter;

	public PlanningServiceImpl(PlanningDAO planningDAO, ParticipantService participantService, OralDefenseService oralDefenseService, PersonMailResolver personResolver, UnavailabilityDAO unavailabilityDAO) {
		this.planningDAO = planningDAO;
		this.participantService = participantService;
		this.oralDefenseService = oralDefenseService;
		this.personResolver = personResolver;
		this.unavailabilityDAO = unavailabilityDAO;
		this.planningSplitter = new PlanningSplitterImpl();
	}

	public Planning findById(Integer id) {
		return planningDAO.findById(id);
	}

	@Override
	public Planning save(Planning planning) {
		Validate.notNull(planning);
		Validate.isTrue(planning.getOralDefenseDuration() > 0);
		planning.getPeriod().validate();
		planning.getDayPeriod().validate();
		planning.getLunchBreak().validate();
		planning.setIs_ref(1);
		planning.setPriorities(Lists.newArrayList(
				new Priority("ENTREPRISE", 1),
				new Priority("ENSEIGNANT", 2),
        		new Priority("HORAIRES", 3)
		));

		planning.setParticipants(participantService.saveParticipants(planning.getParticipants()));

		int timeBoxes = planningSplitter.execute(planning).getTimeBoxes().size();
		planning.setNbMaxOralDefensePerDay(timeBoxes / planningSplitter.getNbDays());

		planningDAO.persist(planning);

		return planning;
	}

	public Planning update(Planning planning) {
		planningDAO.update(planning);

		return planning;

	}

	public List<Planning> findAllByUid(String uid){
		return planningDAO.findAll(uid);
	}

	public Collection<Participant> findParticipants(Planning planning) {

		return planningDAO.findParticipants(planning);
	}

	@Override
	public Planning findByName(String name) {
		return planningDAO.findByName(name);
	}

	@Override
	public Collection<Priority> findPriorities(Planning planning) {
		return planningDAO.findPriorities(planning);
	}

	@Override
	public Planning generate(Integer planningId) {

		// retrieving the planning
		Planning planning = planningDAO.findById(planningId);
		Validate.notNull(planning);

		// check if the current user is the admin of this planning
//		session.acceptOnly(planning.getAdmin());


		// initialize builder
		PlanningExportBuilder builder = new PlanningExportBuilder(planning);
		builder.setParticipants(planningDAO.findParticipants(planning));
		builder.setUnavailabilities(unavailabilityDAO.findByPlanningId(planning.getId()));

		// build & return
		if (!planning.getOralDefenses().isEmpty())
			oralDefenseService.delete(planning.getOralDefenses());

	 	planning.setOralDefenses(oralDefenseService.save(builder.split().build().getOralDefenses(personResolver)));

	 	planningDAO.update(planning);

	 	return planning;
	}

	@Override
	public void updateByPersonUnavailabilities(int planningId, String personUid) {
		Planning planning = planningDAO.findById(planningId);
		Validate.notNull(planning);
		PlanningExportBuilder builder = new PlanningExportBuilder(planning);

		oralDefenseService.update(builder.split().updatePlanning(unavailabilityDAO.findById(planning.getId(), personUid)));

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
			if (!uids.contains(p.getStudent().getUid())) {
				uids.add(p.getStudent().getUid());
			}
			if (!uids.contains(p.getFollowingTeacher().getUid())) {
				uids.add(p.getFollowingTeacher().getUid());
			}
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

	@Override
	public List<Planning> findAll() {
		return planningDAO.findAll();
	}


}
