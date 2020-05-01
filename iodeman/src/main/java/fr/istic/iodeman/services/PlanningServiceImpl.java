package fr.istic.iodeman.services;

import fr.istic.iodeman.builder.PlanningExportBuilder;
import fr.istic.iodeman.models.*;
import fr.istic.iodeman.repositories.*;
import fr.istic.iodeman.resolver.PersonMailResolver;
import fr.istic.iodeman.strategy.PlanningSplitter;
import fr.istic.iodeman.strategy.PlanningSplitterImpl;
import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PlanningServiceImpl implements PlanningService {

	private final PersonRepository personRepository;

	private final PlanningRepository planningRepository;

	private final UnavailabilityRepository unavailabilityRepository;

	private final PriorityRepository priorityRepository;

	private final OralDefenseService oralDefenseService;

	private final PersonMailResolver personResolver;

	private final PlanningSplitter planningSplitter;

	private final ColorRepository colorRepository;

	private final EntityRevisionService entityRevisionService;

	private  PlanningExportBuilder builder;

	public PlanningServiceImpl(PersonRepository personRepository, PlanningRepository planningRepository, UnavailabilityRepository unavailabilityRepository, PriorityRepository priorityRepository, OralDefenseService oralDefenseService, PersonMailResolver personResolver, ColorRepository colorRepository, EntityRevisionService entityRevisionService) {
		this.personRepository = personRepository;
		this.planningRepository = planningRepository;
		this.unavailabilityRepository = unavailabilityRepository;
		this.priorityRepository = priorityRepository;
		this.oralDefenseService = oralDefenseService;
		this.personResolver = personResolver;
		this.planningSplitter = new PlanningSplitterImpl();
		this.colorRepository = colorRepository;
		this.entityRevisionService = entityRevisionService;
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

		Collection<OralDefense> oralDefenses = planning.getOralDefenses();

		planning.setOralDefenses(null);

		int timeBoxes = planningSplitter.execute(planning).getTimeBoxes().size();
		planning.setNbMaxOralDefensePerDay(timeBoxes / planningSplitter.getNbDays());

		planning = planningRepository.save(planning);

		for (OralDefense oralDefense : oralDefenses) {
			oralDefense.setPlanning(planning);
		}


		Iterator<OralDefense> iterator = oralDefenseService.save(oralDefenses).iterator();
		oralDefenses = new ArrayList<>();
		while (iterator.hasNext())
			oralDefenses.add(iterator.next());

		planning.setOralDefenses(oralDefenses);

		entityRevisionService.createRevision(planning);

		return planning;
	}

	@Override
	public Planning update(Planning planning) {
		planning = planningRepository.save(planning);
		entityRevisionService.createRevision(planning);
		return planning;
	}

	@Override
	public List<Planning> findPersonBy(String uid){
		return planningRepository.findByPerson(uid);
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
		builder = new PlanningExportBuilder(colorRepository);
		builder.setPlanning(planning);
		builder.setOralDefenses(planning.getOralDefenses());
		builder.setUnavailabilities(unavailabilityRepository.findByPlanning(planning));

		// build & return
		builder.split().build();

		oralDefenseService.save(builder.split().build().getOralDefenses(personResolver));

		return planning;
	}

	@Override
	public void createRevision(Long id) {
		// retrieving the planning
		Planning planning = this.findById(id);
		Validate.notNull(planning);

		// initialize builder
		builder = new PlanningExportBuilder(colorRepository);
		builder.setPlanning(planning);
		builder.setOralDefenses(planning.getOralDefenses());
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
		builder = new PlanningExportBuilder(colorRepository);
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



}
