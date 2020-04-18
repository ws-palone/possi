package fr.istic.iodeman.services;

import com.google.common.collect.Lists;
import fr.istic.iodeman.builder.PlanningExportBuilder;
import fr.istic.iodeman.models.*;
import fr.istic.iodeman.repositories.PersonRepository;
import fr.istic.iodeman.repositories.PlanningRepository;
import fr.istic.iodeman.repositories.PriorityRepository;
import fr.istic.iodeman.repositories.UnavailabilityRepository;
import fr.istic.iodeman.resolver.PersonMailResolver;
import fr.istic.iodeman.strategy.PlanningSplitter;
import fr.istic.iodeman.strategy.PlanningSplitterImpl;
import org.apache.commons.lang.Validate;
import org.springframework.data.history.Revisions;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PlanningServiceImpl implements PlanningService {

	private final PersonRepository personRepository;

	private final PlanningRepository planningRepository;

	private final UnavailabilityRepository unavailabilityRepository;

	private final PriorityRepository priorityRepository;

	private final ParticipantService participantService;

	private final OralDefenseService oralDefenseService;

	private final PersonMailResolver personResolver;

	private final PlanningSplitter planningSplitter;

	private final PlanningExportBuilder builder;

	public PlanningServiceImpl(PersonRepository personRepository, PlanningRepository planningRepository, UnavailabilityRepository unavailabilityRepository, PriorityRepository priorityRepository, ParticipantService participantService, OralDefenseService oralDefenseService, PersonMailResolver personResolver, PlanningExportBuilder builder) {
		this.personRepository = personRepository;
		this.planningRepository = planningRepository;
		this.unavailabilityRepository = unavailabilityRepository;
		this.priorityRepository = priorityRepository;
		this.participantService = participantService;
		this.oralDefenseService = oralDefenseService;
		this.personResolver = personResolver;
		this.planningSplitter = new PlanningSplitterImpl();
		this.builder = builder;
	}

	@Override
	public Planning findById(Long id) {
		Optional<Planning> planning =  planningRepository.findById(id);
		return planning.orElse(null);
	}

	@Override
	public Planning save(Planning planning) {
		Validate.notNull(planning);
		Validate.isTrue(planning.getOralDefenseDuration() > 0);
		planning.getPeriod().validate();
		planning.getDayPeriod().validate();
		planning.getLunchBreak().validate();
//		Todo charger depuis la bd
		Collection<Priority> priorities = new ArrayList<>();
		priorityRepository.findAll().forEach(priorities::add);
		planning.setPriorities(priorities);

		Collection<Participant> participants = new ArrayList<>();
		participantService.saveParticipants(planning.getParticipants()).forEach(participants::add);
		planning.setParticipants(participants);

		int timeBoxes = planningSplitter.execute(planning).getTimeBoxes().size();
		planning.setNbMaxOralDefensePerDay(timeBoxes / planningSplitter.getNbDays());

		return planningRepository.save(planning);
	}

	@Override
	public Planning update(Planning planning) {
		return planningRepository.save(planning);
	}

	@Override
	public List<Planning> findAdminBy(String uid){
		return planningRepository.findByAdmin_Uid(uid);
	}

	@Override
	public Planning findByName(String name) {
		return planningRepository.findByName(name);
	}

	@Override
	public Planning generate(Long planningId) {

		// retrieving the planning
		Planning planning = this.findById(planningId);
		Validate.notNull(planning);

		// initialize builder
		builder.setPlanning(planning);
		builder.setParticipants(planning.getParticipants());
		builder.setUnavailabilities(unavailabilityRepository.findByPlanning(planning));

		// build & return
		builder.split().build();

		if (!planning.getOralDefenses().isEmpty())
			oralDefenseService.delete(planning.getOralDefenses());
		Collection<OralDefense> oralDefenses = new ArrayList<>();
		oralDefenseService.save(builder.split().build().getOralDefenses(personResolver)).forEach(oralDefenses::add);
		planning.setOralDefenses(builder.split().build().getOralDefenses(personResolver));

		return planning;
	}

	@Override
	public void createRevision(Long id) {
		// retrieving the planning
		Planning planning = this.findById(id);
		Validate.notNull(planning);

		// initialize builder
		builder.setPlanning(planning);
		builder.setParticipants(planning.getParticipants());
		builder.setUnavailabilities(unavailabilityRepository.findByPlanning(planning));

		Collection<OralDefense> oralDefenses = new ArrayList<>();
		oralDefenseService.save(builder.split().build().getOralDefenses(personResolver)).forEach(oralDefenses::add);
		planning.setOralDefenses(oralDefenses);

		planningRepository.save(planning);
	}

	@Override
	public void updateByPersonUnavailabilities(Long planningId, String personUid) {
		Planning planning = this.findById(planningId);
		Validate.notNull(planning);
		builder.setPlanning(planning);
		oralDefenseService.save(builder.split().updatePlanning(unavailabilityRepository.findByPlanningAndPerson(planning, personRepository.findByUid(personUid))));

	}

	@Override
	public void delete(Planning planning) {
		planningRepository.delete(planning);
	}

	@Override
	public Iterable<Planning> findAll() {
		return planningRepository.findAll();
	}

	@Override
	public Revisions<Long, Planning> findRevision(Long id) {
		return planningRepository.findRevisions(id);
	}


}
