package fr.istic.iodeman.cas;

import org.springframework.stereotype.Component;

import edu.yale.its.tp.cas.client.ServiceTicketValidator;

@Component
public class TicketValidatorFactory {
	
	private ServiceTicketValidator sv;
	
	private final String SERVER_NAME = "https://sso-cas.univ-rennes1.fr/";
	private final String SERVER_NAME_LOGIN = "https://sso-cas.univ-rennes1.fr/login";
	private final String SERVER_NAME_VALIDATE = "https://sso-cas.univ-rennes1.fr/serviceValidate";
	
	private final String SERVICE_NAME = "http://iode-man-debian.istic.univ-rennes1.fr:8080/iodeman/login";
		
	public ServiceTicketValidator getServiceTicketValidator(String ticket) {
		
		sv = new ServiceTicketValidator();
		
		sv.setCasValidateUrl(SERVER_NAME_VALIDATE);
		sv.setService(SERVICE_NAME);
		sv.setServiceTicket(ticket);
		
		return sv;
	}
	
	public String getLoginPage(){
		return SERVER_NAME_LOGIN+"?service="+SERVICE_NAME;
	}

}
