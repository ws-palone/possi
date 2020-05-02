package fr.istic.iodeman.services;


import fr.istic.iodeman.models.Planning;
import fr.istic.iodeman.models.Unavailability;

import java.util.Collection;
import java.util.List;

public interface UnavailabilityService {

	List<Unavailability> findById(Long idPlanning, String uid);

	void save(Long idPlanning, Collection<Unavailability> unavailabilities);

	void delete(Long id);

	void delete(Long idPlanning, Collection<Unavailability> unavailabilities);

	void deleteByPlanning(Long planningId);

    Planning setUnavailabilityByOralDefenses(Planning planning);
}
