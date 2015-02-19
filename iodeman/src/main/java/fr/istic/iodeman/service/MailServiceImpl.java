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

	@Override
	public String sendToEveryParticipant(Planning planning) {
		
		Validate.notNull(planning);
		
		String  MAIL_TYPE = "new";
		String MAIL_OBJECT = "Notification : Soutenance de stage";
		String MAIL_TO = "";

		Collection<Participant> participants = planningDAO.findParticipants(planning);
		
		Collection<String> participantsMails = Lists.newArrayList();

		Validate.notNull(participants);
		
		for(Participant p : participants){
			participantsMails.add(p.getStudent().getEmail());
			participantsMails.add(p.getFollowingTeacher().getEmail());
		}
		
		participantsMails = getSingleMailFromMailList(participantsMails);
		
		StringBuilder builder = new StringBuilder();
		for(String p : participantsMails){
			builder.append(p).append(',');
		}	
		
		if (builder.length() > 0) {
			builder.deleteCharAt(builder.length()-1);
		}
		
		MAIL_TO = builder.toString();
		
		return MAIL_SERVER+"?type="+MAIL_TYPE+"&subject="+MAIL_OBJECT+"&to="+MAIL_TO;
	}
	
	private Collection<String> getSingleMailFromMailList(Collection<String> participantMail){

		return Lists.newArrayList(ImmutableSortedSet.copyOf(participantMail));
	}

}
