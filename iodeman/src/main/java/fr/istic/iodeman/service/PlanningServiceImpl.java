package fr.istic.iodeman.service;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.istic.iodeman.dao.PlanningDAO;
import fr.istic.iodeman.model.Participant;
import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.resolver.PersonResolver;
import fr.istic.iodeman.strategy.ParticipantsExcelImport;
import fr.istic.iodeman.strategy.ParticipantsImport;

@Service
public class PlanningServiceImpl implements PlanningService {

	@Autowired
	private PlanningDAO planningDAO;
	
	@Autowired
	private PersonResolver personResolver;
	
	public List<Planning> findAll() {
		
		return planningDAO.findAll();
	}

	public Planning findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Planning create(String name) {
		
		Planning planning = new Planning();
		planning.setName(name);
		planningDAO.persist(planning);
		return planning;
	}

	public Planning importPartcipants(Planning planning, File file) throws Exception {
		
		ParticipantsImport participantsImport = new ParticipantsExcelImport();
		participantsImport.configure(personResolver);
		Collection<Participant> participants = participantsImport.execute(file);
		
		planning.setParticipants(participants);
		
		// check existing participants in DB
		// check participants on LDAP
		
		planningDAO.persist(planning);
		
		return planning;
	}

}
