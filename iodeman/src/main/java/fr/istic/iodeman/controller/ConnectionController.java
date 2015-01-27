package fr.istic.iodeman.controller;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.CookieGenerator;
import org.xml.sax.SAXException;

import edu.yale.its.tp.cas.client.ServiceTicketValidator;
import fr.istic.iodeman.cas.TicketValidatorFactory;

@Controller
public class ConnectionController {
	
	@Autowired
	private TicketValidatorFactory ticketValidatorFactory;
	
	@RequestMapping("/login")
	public String validate(@RequestParam(value="ticket", defaultValue="") String ticket, HttpServletRequest request) throws IOException, SAXException, ParserConfigurationException{
		 		
		if (!ticket.equals("")) {
			
			ServiceTicketValidator validator = ticketValidatorFactory.getServiceTicketValidator(ticket);
			
			validator.validate();
			
			if (validator.isAuthenticationSuccesful()) {
				HttpSession session = request.getSession();
			    session.setAttribute("cas_ticket", ticket);
			    session.setAttribute("uid", validator.getUser()); 
			    
			    return "forward:/public/index.html";
			}
			
		}
		
		return "redirect:"+ticketValidatorFactory.getLoginPage();

	}
	
	
	
	/*
	@RequestMapping("/")
	public String home() {
		
		return "forward:/public/index.html";
	}*/
	
}
