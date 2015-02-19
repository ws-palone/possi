package fr.istic.iodeman.service;

import java.util.Collection;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Lists;

import fr.istic.iodeman.dao.PlanningDAO;
import fr.istic.iodeman.model.Participant;
import fr.istic.iodeman.model.Planning;

@Service
public class MailServiceImpl implements MailService{
	
	@Autowired
	private PlanningDAO planningDAO;
	
	@Value("${MAIL_SERVICE}")
	private String MAIL_SERVER;
	
	private String MAIL_TYPE;
	private String MAIL_TO;
	private String MAIL_OBJECT;

	@SuppressWarnings("null")
	@Override
	public String sendToEveryParticipant(Integer idPlanning) {
		
		Validate.notNull(idPlanning);
		
		MAIL_TYPE = "new";
		MAIL_OBJECT = "Notification : Soutenance de stage";
		MAIL_TO = "";
		
		Planning planning = planningDAO.findById(idPlanning);
		
		Validate.notNull(planning);
		
		Collection<Participant> participants = null;
		participants = planningDAO.findParticipants(planning);
		
		Collection<String> participantMailStudent = null;
		Collection<String> participantMailTeacher = null;

		Validate.notNull(participants);
		
		for(Participant p : participants){
			participantMailStudent.add(p.getStudent().getEmail());
			participantMailTeacher.add(p.getFollowingTeacher().getEmail());
		}
		
		participantMailStudent = getSingleMailFromMailList(participantMailStudent);
		participantMailTeacher = getSingleMailFromMailList(participantMailTeacher);
		
		for(String p : participantMailStudent){
			MAIL_TO+= p+",";
		}
		
		for(String p : participantMailTeacher){
			MAIL_TO+= p+",";
		}
		
		return "redirect:"+MAIL_SERVER+"?type="+MAIL_TYPE+"&subject="+MAIL_OBJECT+"&to="+MAIL_TO;
	}
	
	private Collection<String> getSingleMailFromMailList(Collection<String> participantMail){

		return Lists.newArrayList(ImmutableSortedSet.copyOf(participantMail));
	}
	
	

}
