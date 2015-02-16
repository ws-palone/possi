package fr.istic.iodeman.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.istic.iodeman.service.MailService;

@RestController
public class MailController {
	
	@Autowired
	private MailService mailService;
	
	
	
	@RequestMapping("/mail")
	public String senMailToEveryParticipant(@RequestParam("planningID") Integer planningID){
		
		return mailService.sendToEveryParticipant(planningID);
		
	}

}
