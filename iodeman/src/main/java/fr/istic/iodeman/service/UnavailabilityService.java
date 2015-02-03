package fr.istic.iodeman.service;

import java.util.List;

import fr.istic.iodeman.model.Unavailability;

public interface UnavailabilityService {

	public List<Unavailability> findById(Integer id, Integer idperson);

}
