package fr.istic.iodeman.mail;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MailController {
	
	private String MAIL_SERVER;
	private String MAIL_TYPE;
	private String MAIL_TO;
	
	@RequestMapping("/mail")
	public String redirectToNewMail(){
		
		MAIL_SERVER = "https://webmail.etudiant.univ-rennes1.fr/dimp/compose.php";
		MAIL_TYPE = "new";
		MAIL_TO = "lecut.alexandre@gmail.com";
		
		return "redirect:"+MAIL_SERVER+"?type="+MAIL_TYPE+"&to="+MAIL_TO;
		
	}

}
