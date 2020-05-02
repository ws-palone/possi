package fr.istic.iodeman.services;

import fr.istic.iodeman.models.OralDefense;
import fr.istic.iodeman.models.Person;
import fr.istic.iodeman.models.Planning;
import fr.istic.iodeman.models.Unavailability;
import fr.istic.iodeman.repositories.PersonRepository;
import fr.istic.iodeman.repositories.UnavailabilityRepository;
import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UnavailabilityServiceImpl implements UnavailabilityService{

	private final UnavailabilityRepository unavailabilityRepository;

	private final PlanningService planningService;

	private PersonRepository personRepository;

	public UnavailabilityServiceImpl(UnavailabilityRepository unavailabilityRepository, PlanningService planningService, PersonRepository personRepository) {
		this.unavailabilityRepository = unavailabilityRepository;
		this.planningService = planningService;
		this.personRepository = personRepository;
	}

	@Override
	public List<Unavailability> findById(Long idPlanning, String uid) {
		Person person = personRepository.findByUid(uid);
		Planning planning = planningService.findById(idPlanning);
		Validate.notNull(person);
		Validate.notNull(planning);
		return unavailabilityRepository.findByPlanningAndPerson(planning, person);
	}

	@Override
	public void save(Long idPlanning, Collection<Unavailability> unavailabilities) {
		//Validation
		Validate.notNull(idPlanning);
		if (!unavailabilities.isEmpty()) {
			Planning planning = planningService.findById(idPlanning);
			Validate.notNull(planning);
			for (Unavailability unavailability : unavailabilities)
				unavailability.setPlanning(planning);
			unavailabilityRepository.saveAll(unavailabilities);
		}
	}

	@Override
	public void delete(Long idPlanning, Collection<Unavailability> unavailabilities) {

		//Validation
		Validate.notNull(idPlanning);
		if (!unavailabilities.isEmpty()) {
			Planning planning = planningService.findById(idPlanning);
			Validate.notNull(planning);

			// delete all the unavailabilities that make the period unavailable
			for(Unavailability ua : unavailabilities) {
				ua.setPlanning(planning);
				unavailabilityRepository.delete(ua);
			}
		}
	}

	@Override
	public void deleteByPlanning(Long planningId) {
		unavailabilityRepository.deleteByPlanning(planningService.findById(planningId));
	}

	@Override
	public Planning setUnavailabilityByOralDefenses(Planning planning) {

		if (planning != null) {
			Collection<OralDefense> oralDefenses = planning.getOralDefenses();
			for (OralDefense oralDefense : oralDefenses) {
				if(oralDefense.getNumber() != null) {
					Person followingTeacher = oralDefense.getFollowingTeacher();
					oralDefense.setUnavailabilities(this.findById(planning.getId(), followingTeacher.getUid())
							.stream().map(Unavailability::getPeriod).collect(Collectors.toList()));

					addOtherOralDefenseAsUnavailabilities(oralDefense, oralDefenses, followingTeacher);

					Person secondTeacher = oralDefense.getSecondTeacher();
					if (secondTeacher != null) {
						oralDefense.getUnavailabilities().addAll(this.findById(planning.getId(), secondTeacher.getUid())
								.stream().map(Unavailability::getPeriod).collect(Collectors.toList()));

						addOtherOralDefenseAsUnavailabilities(oralDefense, oralDefenses, secondTeacher);
					}
				}
			}
		}
		return planning;
	}

	private void addOtherOralDefenseAsUnavailabilities(OralDefense oralDefense, Collection<OralDefense> oralDefenses, Person teacher) {
		for (OralDefense o : oralDefenses) {
			if (!o.equals(oralDefense)) {
				Person secondTeacher = o.getSecondTeacher();
				if ((secondTeacher != null && secondTeacher.getUid().equals(teacher.getUid())) ||
						o.getFollowingTeacher().getUid().equals(teacher.getUid())) {
					oralDefense.getUnavailabilities().add(o.getTimeBox());
				}
			}
		}
	}
}
