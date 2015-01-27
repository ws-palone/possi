package fr.istic.iodeman.controller;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.xml.sax.SAXException;

import edu.yale.its.tp.cas.client.ServiceTicketValidator;

@Controller
public class ConnectionController {
	
	public class Connection{
		
	}
	
	@RequestMapping("/")
	public String validate(@RequestParam(value="ticket", defaultValue="") String ticket) throws IOException, SAXException, ParserConfigurationException{
		
		 String serverName = "https://sso-cas.univ-rennes1.fr/";
		 String serverNameLogin = "https://sso-cas.univ-rennes1.fr/login";
		 String serverNameValidate = "https://sso-cas.univ-rennes1.fr/serviceValidate";
		 
		 String serviceName = "http://iode-man-debian.istic.univ-rennes1.fr:8080/iodeman/";
		// String serviceNameLogin = "http://iode-man-debian.istic.univ-rennes1.fr:8080/iodeman/login";
		 
		 String user = null;
		 String errorCode = null;
		 String errorMessage = null;
		 String xmlResponse = null;
		 
		 if (ticket.equals(""))
			{  	
			 	// If no ticket, redirect to ENT Log
			 	return "redirect:"+serverNameLogin+"?service=" +serviceName;
			}
		  
		 ServiceTicketValidator sv = new ServiceTicketValidator();
		  
		 sv.setCasValidateUrl(serverNameValidate);
		 sv.setService(serviceName);
		 sv.setServiceTicket(ticket);
		  
		 sv.validate();
		  
		 xmlResponse = sv.getResponse();
		  
		 if(sv.isAuthenticationSuccesful()) {
		     user = sv.getUser();
		 } else {
			 return "redirect:"+serverNameLogin + "?service=" +serviceName;
		 }
		 
		 //return "redirect:hello?user_id="+sv.getUser();
		 return "forward:/public/index.html";
	}
	
	
	
	/*
	@RequestMapping("/")
	public String home() {
		
		return "forward:/public/index.html";
	}*/
	
}
