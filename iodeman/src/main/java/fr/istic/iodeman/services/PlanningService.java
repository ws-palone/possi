package fr.istic.iodeman.services;


import fr.istic.iodeman.models.Planning;

import java.util.List;

public interface PlanningService {

	Planning findById(Long id);

	Planning save(Planning planning);

	Planning update(Planning planning);

	List<Planning> findPersonBy(String uid);

	Planning findByName(String name);

	Planning generate(Long planningId);

	void updateByPersonUnavailabilities(Long planningId, String personUid);

	void delete(Planning planning);

	Iterable<Planning> findAll();
}
