package fr.istic.iodeman.cas;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import edu.yale.its.tp.cas.client.ServiceTicketValidator;

@Component
public class TicketValidatorFactory {
	
	@Value("${IODEMAN_URL}")
	private String IODEMAN_SERVER_NAME;
	
	@Value("${CAS_SERVICE_LOGIN}")
	private String SERVER_NAME_LOGIN;
	
	@Value("${CAS_SERVICE_VALIDATE}")
	private String SERVER_NAME_VALIDATE;
	
	@Value("${CAS_SERVICE_LOGOUT}")
	private String SERVER_NAME_LOGOUT;
	
	private ServiceTicketValidator sv;
	
	/*private final String SERVER_NAME = "https://sso-cas.univ-rennes1.fr/";
	private final String SERVER_NAME_LOGIN = "https://sso-cas.univ-rennes1.fr/login";
	private final String SERVER_NAME_VALIDATE = "https://sso-cas.univ-rennes1.fr/serviceValidate";*/
		
	public ServiceTicketValidator getServiceTicketValidator(String ticket) {
		
		sv = new ServiceTicketValidator();
		
		sv.setCasValidateUrl(SERVER_NAME_VALIDATE);
		sv.setService(getLoginURL());
		sv.setServiceTicket(ticket);
		sv.setRenew(false);
		
		return sv;
	}
	
	private String getLoginURL() {
		StringBuilder builder = new StringBuilder(IODEMAN_SERVER_NAME);
		if (builder.charAt(builder.length()-1) != '/') {
			builder.append('/');
		}
		builder.append("api/auth/login");
		return builder.toString();
	}
	
	public String getLoginPage(){
		return SERVER_NAME_LOGIN+"?service="+getLoginURL();
	}
	
	public String getLogoutPage(){
		return SERVER_NAME_LOGOUT;
	}

}
