package fr.istic.iodeman.service;

import java.util.Collection;
import java.util.List;

import fr.istic.iodeman.dto.AgendaDTO;
import fr.istic.iodeman.model.TimeBox;
import fr.istic.iodeman.model.Unavailability;

public interface UnavailabilityService {
	
	public List<Unavailability> findById(Integer id, String uid);

	public Unavailability create(Integer id, String uidperson, TimeBox period);

	public Unavailability delete(Integer id);
	
	public Collection<Unavailability> delete(Integer planningId, String uid, TimeBox period);
	
	public Collection<AgendaDTO> exportAgenda(Integer planningId, String personId);
	
	public void deleteByPlanning(Integer planningId);

}
