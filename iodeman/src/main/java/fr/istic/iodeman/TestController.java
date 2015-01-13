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
	public ServiceTicketValidator validate(@RequestParam(value="ticket", defaultValue="") String ticket) throws IOException, SAXException, ParserConfigurationException{
		
		 String serverName = "https://sso-cas.univ-rennes1.fr/serviceValidate";
		 String serviceName = "http://iode-man.istic.univ-rennes1.fr:8080/idoeman";
		 String user = null;
		 String errorCode = null;
		 String errorMessage = null;
		 String xmlResponse = null;
		 
		 if (ticket == null )
			{  	
			 	//return "redirect:"+serverName + "?service=" +serviceName; //
			}
		  
		 ServiceTicketValidator sv = new ServiceTicketValidator();
		  
		 sv.setCasValidateUrl(serverName);
		 sv.setService(serviceName);
		 sv.setServiceTicket(ticket);
		  
		 sv.validate();
		  
		 xmlResponse = sv.getResponse();
		  
		 if(sv.isAuthenticationSuccesful()) {
		     user = sv.getUser();
		 } else {
		     errorCode = sv.getErrorCode();
		     errorMessage = sv.getErrorMessage();
		 }
		 
		 return sv;
		 
	}
}  
