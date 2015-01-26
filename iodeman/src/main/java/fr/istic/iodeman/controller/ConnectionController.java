package fr.istic.iodeman.controller;

import java.io.IOException;
import java.util.List;

import javax.naming.directory.DirContext;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import edu.yale.its.tp.cas.client.ServiceTicketValidator;
import fr.istic.iodeman.service.LDAPServ;

@Controller
public class ConnectionController {
	
	@Autowired
	private LDAPServ ldapserv;
	
	public class Connection{
		
	}

	@RequestMapping("/hello")
	public String hello(@RequestParam(value="user_id", defaultValue="") String user_id){
		
		return user_id;
		
		/*List<String> list = ldapserv.getAllPersonNames();
		
		String listString = "List : ";

		for (String s : list)
		{
		    listString += s + "\t";
		}

	    return listString;*/
	    
	    //return ldapserv.lookupPerson(user_id);
		
	}
	
	@RequestMapping("/login")
	public String validate(@RequestParam(value="ticket", defaultValue="") String ticket) throws IOException, SAXException, ParserConfigurationException{
		
		 String serverName = "https://sso-cas.univ-rennes1.fr/";
		 String serverNameLogin = "https://sso-cas.univ-rennes1.fr/login";
		 String serverNameValidate = "https://sso-cas.univ-rennes1.fr/serviceValidate";
		 
		 String serviceName = "http://iode-man-debian.istic.univ-rennes1.fr:8080/iodeman/";
		 String serviceNameLogin = "http://iode-man-debian.istic.univ-rennes1.fr:8080/iodeman/login";
		 
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
		 sv.setService(serviceNameLogin);
		 sv.setServiceTicket(ticket);
		  
		 sv.validate();
		  
		 xmlResponse = sv.getResponse();
		  
		 if(sv.isAuthenticationSuccesful()) {
		     user = sv.getUser();
		 } else {
			 return "redirect:"+serverNameLogin + "?service=" +serviceName;
		 }
		 
		 return "redirect:hello?user_id="+sv.getUser();
		 
	}
	
	@RequestMapping("/home")
	public String home() {
		
		return "forward:/index.html";
	}
	
}
