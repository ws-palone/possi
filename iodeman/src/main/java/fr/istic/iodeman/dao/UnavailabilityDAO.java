package fr.istic.iodeman.dao;

import java.util.Date;
import java.util.List;

import fr.istic.iodeman.model.Unavailability;
import fr.istic.possijar.Creneau;

public interface UnavailabilityDAO {
	
     public List<Unavailability> findAll();
	
	public void persist(Unavailability unav);
	
	public Unavailability findById(Integer ID);
	
	public void delete(Unavailability unav);
	
	public void deleteAll();

	public List<Unavailability> findById(Integer idPlanning, String uid);
	
	public List<Unavailability> findByPlanningId(Integer idPlanning);

	public void deleteByPlanning(Integer planningId);

	void deleteAll(Integer id, Integer ref_id);

    List<Date> getUnavailabilities(Integer planningId, Creneau creneau);
}
