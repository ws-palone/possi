package fr.istic.iodeman.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.istic.iodeman.service.MailService;

@Controller
public class MailController {
	
	@Autowired
	private MailService mailService;
	
	@RequestMapping("/mail/{id}")
	public String senMailToEveryParticipant(@PathVariable("planningID") Integer planningID){
		
		return mailService.sendToEveryParticipant(planningID);
		
	}

}
