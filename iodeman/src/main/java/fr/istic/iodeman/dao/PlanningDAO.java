package fr.istic.iodeman.dao;

import fr.istic.iodeman.model.Participant;
import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.Priority;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface PlanningDAO {

	public List<Planning> findAll();
	
	public List<Planning> findAll(String uid);
	
	public Integer persist(Planning pla);
	
	public void update(Planning planning);
	
	public Planning findById(Integer ID);
	
	public Collection<Participant> findParticipants(Planning planning);
	
	public Map<String, Integer> findParticipantsAndUnavailabilitiesNumber(Planning planning, Collection<String> uids);
	
	public void delete(Planning pla);
	
	public void deleteAll();
	
	public Collection<Priority> findPriorities(Planning planning);

    public Integer duplicate(Integer id);
    public Integer duplicateDraft(Integer id);

    public List<Planning> findDrafts(Integer id);

	public void switchReference(Integer idDraft);


    public void deleteDraft(Integer id);
}
