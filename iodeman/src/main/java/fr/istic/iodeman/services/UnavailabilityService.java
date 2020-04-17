package fr.istic.iodeman.services;

import fr.istic.iodeman.model.Unavailability;

import java.util.Collection;
import java.util.List;

public interface UnavailabilityService {
	
	List<Unavailability> findById(Integer id, String uid);

	void save(Integer planningId, Collection<Unavailability> unavailabilities);

	Unavailability delete(Integer id);
	
	Collection<Unavailability> delete(Integer planningId, Collection<Unavailability> unavailabilities);

	void deleteByPlanning(Integer planningId);

	void deleteAll(Integer id, Integer ref_id);

}
