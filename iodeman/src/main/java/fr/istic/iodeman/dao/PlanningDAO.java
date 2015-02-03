package fr.istic.iodeman.dao;

import java.util.Collection;
import java.util.List;

import fr.istic.iodeman.model.Participant;
import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.Priority;

public interface PlanningDAO {

	public List<Planning> findAll();
	
	public List<Planning> findAll(String uid);
	
	public void persist(Planning pla);
	
	public void update(Planning planning);
	
	public Planning findById(Integer ID);
	
	public Collection<Participant> findParticipants(Planning planning);
	
	public void delete(Planning pla);
	
	public void deleteAll();
	
	public Collection<Priority> findPriorities(Planning planning);

}
