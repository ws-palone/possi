package fr.istic.iodeman.service;

import java.util.Collection;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.istic.iodeman.dao.PlanningDAO;
import fr.istic.iodeman.model.Participant;
import fr.istic.iodeman.model.Person;
import fr.istic.iodeman.model.Planning;

@Service
public class MailServiceImpl implements MailService{
	
	@Autowired
	private PlanningDAO planningDAO;
	
	private String MAIL_SERVER;
	private String MAIL_TYPE;
	private String MAIL_TO;
	private String MAIL_OBJECT;

	@Override
	public String sendToEveryParticipant(Integer idPlanning) {
		
		Validate.notNull(idPlanning);
		
		MAIL_SERVER = "https://webmail.etudiant.univ-rennes1.fr/dimp/compose.php";
		MAIL_TYPE = "new";
		MAIL_OBJECT = "Notification : Soutenance de stage";
		MAIL_TO = "";
		
		Planning planning = planningDAO.findById(idPlanning);
		
		Validate.notNull(planning);
		
		Collection<Participant> participants = null;
		participants = planningDAO.findParticipants(planning);
		
		Validate.notNull(participants);
		
		for(Participant p : participants){
			MAIL_TO+= p.getStudent().getEmail()+",";
		}
		
		return "redirect:"+MAIL_SERVER+"?type="+MAIL_TYPE+"&subject="+MAIL_OBJECT+"&to="+MAIL_TO;
	}
	
	

}
