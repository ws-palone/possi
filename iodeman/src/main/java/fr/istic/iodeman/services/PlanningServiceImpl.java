package fr.istic.iodeman.services;

import fr.istic.iodeman.builder.PlanningExportBuilder;
import fr.istic.iodeman.models.*;
import fr.istic.iodeman.repositories.*;
import fr.istic.iodeman.resolver.PersonMailResolver;
import fr.istic.iodeman.strategy.PlanningSplitter;
import fr.istic.iodeman.strategy.PlanningSplitterImpl;
import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
		return  planningRepository.findById(id).get();
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

		return planning;
	}

	@Override
	public Planning updateWithRevision(Planning planning) {
		planning = this.update(planning);
		entityRevisionService.createRevision(planning.getId());
		return planning;
	}

	@Override
	public Planning update(Planning planning) {
		return planningRepository.save(planning);
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
	@Transactional
	public Planning generate(Long planningId) {

		// retrieving the planning
		Planning planning = this.findById(planningId);
		Validate.notNull(planning);

		// initialize builder
		builder = new PlanningExportBuilder(colorRepository);
		builder.setPlanning(planning);
		builder.setOralDefenses(planning.getOralDefenses());
		builder.setUnavailabilities(unavailabilityRepository.findByPlanning(planning));

		oralDefenseService.save(builder.split().build().getOralDefenses(personResolver));
		planning.setGenerated(true);
		return this.update(planning);
	}

	@Override
	public void updateByPersonUnavailabilities(Long planningId, String personUid) {
		Planning planning = this.findById(planningId);
		Validate.notNull(planning);
		builder = new PlanningExportBuilder(colorRepository);
		builder.setPlanning(planning);
		oralDefenseService.save(builder.split().updatePlanningByUnavailability(unavailabilityRepository.findByPlanningAndPerson(planning, personRepository.findByUid(personUid))));
		planning.setNewUnavailabilities(true);
		this.update(planning);
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
