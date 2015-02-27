package fr.istic.iodeman.service;

import java.util.Collection;

import org.apache.commons.lang.Validate;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Lists;

import fr.istic.iodeman.dao.PlanningDAO;
import fr.istic.iodeman.model.Participant;
import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.Unavailability;

@Service
public class MailServiceImpl implements MailService{
	
	@Autowired
	private PlanningDAO planningDAO;
	
	@Value("${MAIL_SERVICE}")
	private String MAIL_SERVER;
	
	@Autowired
	private JavaMailSender javaMailSender;

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

	@Override
	public void notifyNewUnavailability(Unavailability unavailability) {
		
		Planning planning = unavailability.getPlanning();
		DateTime dateFrom = new DateTime(unavailability.getPeriod().getFrom());
		DateTime dateTo = new DateTime(unavailability.getPeriod().getTo());
		
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(planning.getAdmin().getEmail());
		message.setFrom("IODEMAN");
		message.setSubject("[IODEMAN] "+planning.getName()+" : modifications détectées");
		
		StringBuilder contentBuilder = new StringBuilder();
		contentBuilder.append(unavailability.getPerson().getFirstName());
		contentBuilder.append(" ").append(unavailability.getPerson().getLastName());
		contentBuilder.append(" ne sera pas disponible le ");
		contentBuilder.append(dateFrom.toString("dd/MM/yyyy"));
		contentBuilder.append(" de ").append(dateFrom.toString("HH:mm"));
		contentBuilder.append(" à ").append(dateTo.toString("HH:mm"));
		
		message.setText(contentBuilder.toString());
		
		javaMailSender.send(message);

	}

	@Override
	public void notifyUnavailabityRemoved(Unavailability unavailability) {
		
		Planning planning = unavailability.getPlanning();
		DateTime dateFrom = new DateTime(unavailability.getPeriod().getFrom());
		DateTime dateTo = new DateTime(unavailability.getPeriod().getTo());
		
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(planning.getAdmin().getEmail());
		message.setFrom("IODEMAN");
		message.setSubject("[IODEMAN] "+planning.getName()+" : modifications détectées");
		
		StringBuilder contentBuilder = new StringBuilder();
		contentBuilder.append(unavailability.getPerson().getFirstName());
		contentBuilder.append(" ").append(unavailability.getPerson().getLastName());
		contentBuilder.append(" a supprimé son indisponibilité du ");
		contentBuilder.append(dateFrom.toString("dd/MM/yyyy"));
		contentBuilder.append(" de ").append(dateFrom.toString("HH:mm"));
		contentBuilder.append(" à ").append(dateTo.toString("HH:mm"));
		
		message.setText(contentBuilder.toString());
		
		javaMailSender.send(message);
	}
	

}
