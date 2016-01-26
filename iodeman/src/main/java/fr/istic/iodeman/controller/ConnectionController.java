package fr.istic.iodeman.controller;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.xml.sax.SAXException;

import edu.yale.its.tp.cas.client.ServiceTicketValidator;
import fr.istic.iodeman.SessionComponent;
import fr.istic.iodeman.cas.TicketValidatorFactory;

@Controller
public class ConnectionController {
	
	@Autowired
	private TicketValidatorFactory ticketValidatorFactory;
	
	@Autowired
	private SessionComponent session;
	
	@RequestMapping("/api/auth/login")
	public String validate(@RequestParam(value="ticket", defaultValue="") String ticket) throws IOException, SAXException, ParserConfigurationException{
		
		if (!ticket.equals("")) {
			
			ServiceTicketValidator validator = ticketValidatorFactory.getServiceTicketValidator(ticket);
			
			validator.validate();
			
			if (validator.isAuthenticationSuccesful()) {
				session.init(ticket, validator.getUser());
			    return "redirect:/";
			}
		}
		session.destroy();
		return "redirect:"+ticketValidatorFactory.getLoginPage();

	}
	
	@RequestMapping("/")
	public String home(){
		//System.err.println("On redirige vers login");
		return "/app/index.html";
	}
	
	@RequestMapping("/api/auth/logout")
	public String logout(){
		System.err.println("On redirige vers logout");
		session.destroy();
		return "redirect:"+ticketValidatorFactory.getLogoutPage();
	}
	
}
