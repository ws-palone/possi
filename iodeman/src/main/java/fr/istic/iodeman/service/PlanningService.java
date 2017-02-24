package fr.istic.iodeman.service;

import fr.istic.iodeman.dto.ParticipantDTO;
import fr.istic.iodeman.model.*;
import fr.istic.possijar.Creneau;
import org.json.JSONArray;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface PlanningService {

	public List<Planning> findAll();
	
	public Planning findById(Integer id);
	
	public Planning create(Person admin, String name, TimeBox period, Integer oralDefenseDuration, 
			Integer oralDefenseInterlude, TimeBox lunchBreak,
			TimeBox dayPeriod, Integer nbMaxOralDefensePerDay,
			Collection<Room> rooms);

	public Planning create(Person admin, String name, TimeBox period, Integer oralDefenseDuration,
						   Integer oralDefenseInterlude, TimeBox lunchBreak,
						   TimeBox dayPeriod, Integer nbMaxOralDefensePerDay,
						   Collection<Room> rooms, String csvFile);
	
	public void update(Planning planning, String name,String csvFile, TimeBox period, Integer oralDefenseDuration,
			Integer oralDefenseInterlude, TimeBox lunchBreak,
			TimeBox dayPeriod, Integer nbMaxOralDefensePerDay,
			Collection<Room> rooms);
	
	public Planning importPartcipants(Planning planning, File file) throws Exception;
	
	public Collection<Participant> findParticipants(Planning planning);
	
	public Collection<Priority> findPriorities(Planning planning);
	
	public Collection<Priority> updatePriorities(Planning planning, Collection<Priority> priorities);
	
	public File exportExcel(Planning planning);

    File exportExcelWithoutBuild(Planning planning);

    public Collection<OralDefense> export(Integer planningId);
	
	public void validate(Planning planning);

	public List<Planning> findAllByUid(String uid);
	
	public Collection<ParticipantDTO> findParticipantsAndUnavailabilitiesNumber(Planning planning);
	
	public void delete(Planning planning);

	/**
	 * @param planning
	 * @return
	 */
	public Map<Integer, List<Creneau>> exportJSON(Planning planning);

	// duplicate a specific plannig to PLAN-DRAF
	public Integer duplicate(Integer id);


	public Integer duplicateDraft(Integer id);

	// find drafts of a specific plannig
	public List<Planning> findAllDrafts(Integer id);

	public void switchReference(Integer idDraft);

    public void updateUnvailibilities(Integer id, JSONArray jsonObject);
}
