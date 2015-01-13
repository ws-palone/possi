package fr.istic.iodeman;

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


@RestController  
public class TestController {  
	
	public class Greeting {
		
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
	}
	
	@RequestMapping("/greeting") 
	public Greeting sayHello(@RequestParam(value="name", defaultValue="World") String name) {  
		Greeting g = new Greeting();
		g.setName(name);
		return g;
	}
	
	@RequestMapping("/login")
	public String login(@RequestParam(value="ticket", defaultValue="") String ticket){
		
		
		
		
		 String serverName = "sso-cas.univ-rennes1.fr";
		 String user = null;
		 String errorCode = null;
		 String errorMessage = null;
		 String xmlResponse = null;
		  
		 ServiceTicketValidator sv = new ServiceTicketValidator();
		  
		 sv.setCasValidateUrl(serverName);
		 sv.setService("http://iode-man.istic.univ-rennes1.fr:8080/idoeman/");
		 sv.setServiceTicket(ticket);
		  
		 try {
			sv.validate();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		 xmlResponse = sv.getResponse();
		  
		 if(sv.isAuthenticationSuccesful()) {
		     user = sv.getUser();
		     return user;
		 } else {
		     errorCode = sv.getErrorCode();
		     errorMessage = sv.getErrorMessage();
		 }
		 
		 
		return ticket;
	}
	
}  
