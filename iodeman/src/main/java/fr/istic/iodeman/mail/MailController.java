package fr.istic.iodeman.mail;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.istic.iodeman.SessionComponent;
import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.service.MailService;
import fr.istic.iodeman.service.PlanningService;

@Controller
public class MailController {
	
	@Autowired
	private PlanningService planningService;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private SessionComponent session;
	
	@RequestMapping("/mail/{id}")
	public String senMailToEveryParticipant(@PathVariable("id") Integer id){
		
		Planning planning = planningService.findById(id);
		Validate.notNull(planning);
		
		session.acceptOnly(planning.getAdmin());
		
		return "redirect:"+mailService.sendToEveryParticipant(planning);
		
	}

}
