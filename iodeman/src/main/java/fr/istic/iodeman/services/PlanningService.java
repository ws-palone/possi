package fr.istic.iodeman.services;

import fr.istic.iodeman.dto.ParticipantDTO;
import fr.istic.iodeman.model.*;

import java.util.Collection;
import java.util.List;

public interface PlanningService {

	Planning findById(Integer id);
	
	Planning save(Planning planning);

	Planning update(Planning planning);

	Collection<Participant> findParticipants(Planning planning);

    Planning findByName(String name);

    Collection<Priority> findPriorities(Planning planning);

    Planning generate(Integer planningId);

    void updateByPersonUnavailabilities(int planningId, String personUid);

    void validate(Planning planning);

	List<Planning> findAllByUid(String uid);
	
	Collection<ParticipantDTO> findParticipantsAndUnavailabilitiesNumber(Planning planning);
	
	void delete(Planning planning);

    List<Planning> findAll();

}
