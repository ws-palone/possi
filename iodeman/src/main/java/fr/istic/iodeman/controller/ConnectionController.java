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
	
	@RequestMapping("/login")
	public String validate(@RequestParam(value="ticket", defaultValue="") String ticket) throws IOException, SAXException, ParserConfigurationException{
		
		System.err.println("Le ticket est égal à '" + ticket + "'");
		
		if (!ticket.equals("")) {
			
			System.err.println("Si le ticket n'est pas vide, call ServiceTickerValidator");
			
			ServiceTicketValidator validator = ticketValidatorFactory.getServiceTicketValidator(ticket);
			
			validator.validate();
			
			if (validator.isAuthenticationSuccesful()) {
				
				System.err.println("Si authentification success");
				
				session.init(ticket, validator.getUser());
				
				System.err.println("On redirige vers /");
	
			    return "redirect:/";
			}
			
			System.err.println("Si authentication fail");
			
		}
		
		System.err.println("On détruit la session");
		
		session.destroy();
		return "redirect:"+ticketValidatorFactory.getLoginPage();

	}
	
	@RequestMapping("/")
	public String home(){
		//System.err.println("On redirige vers login");
		return "/app/index.html";
	}
	
	@RequestMapping("/logout")
	public String logout(){
		System.err.println("On redirige vers logout");
		session.destroy();
		return "redirect:"+ticketValidatorFactory.getLogoutPage();
	}
	
}
