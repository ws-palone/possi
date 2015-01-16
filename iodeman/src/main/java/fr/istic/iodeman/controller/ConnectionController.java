package fr.istic.iodeman.controller;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import edu.yale.its.tp.cas.client.ServiceTicketValidator;

public class ConnectionController {
	
	public class Connection{
		
	}

	@RequestMapping("/hello")
	public String hello(@RequestParam(value="id_user", defaultValue="") String id_user){
		
		return "Hello "+id_user;
	}
	
	@RequestMapping(value= "/login", method = RequestMethod.GET)
	public String validate(@RequestParam(value="ticket", defaultValue="") String ticket) throws IOException, SAXException, ParserConfigurationException{
		
		 String serverName = "https://sso-cas.univ-rennes1.fr/serviceValidate";
		 String serviceName = "http://iode-man.istic.univ-rennes1.fr:8080/iodeman/";
		 String user = null;
		 String errorCode = null;
		 String errorMessage = null;
		 String xmlResponse = null;
		 
		 if (ticket.equals(""))
			{  	
			 	return "redirect:"+serverName + "?service=" +serviceName;
			}
		  
		 ServiceTicketValidator sv = new ServiceTicketValidator();
		  
		 sv.setCasValidateUrl(serverName);
		 sv.setService(serviceName+"login");
		 sv.setServiceTicket(ticket);
		  
		 sv.validate();
		  
		 xmlResponse = sv.getResponse();
		  
		 if(sv.isAuthenticationSuccesful()) {
		     user = sv.getUser();
		 } else {
		     errorCode = sv.getErrorCode();
		     errorMessage = sv.getErrorMessage();
		 }
		 
		 return "redirect:"+serviceName+"login?user_id="+sv.getUser();
		 
	}
	
}
