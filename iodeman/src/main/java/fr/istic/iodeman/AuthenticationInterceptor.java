package fr.istic.iodeman;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import edu.yale.its.tp.cas.client.ServiceTicketValidator;
import fr.istic.iodeman.cas.TicketValidatorFactory;


public class AuthenticationInterceptor extends HandlerInterceptorAdapter {
	
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {
    	
    	boolean isValidated = false;
    	
    	 HttpSession session = request.getSession();
	     String sessionTicket = (String) session.getAttribute("cas_ticket");
	             
	     if(sessionTicket != null){	 
	    	 TicketValidatorFactory ticketValidatorFactory = new TicketValidatorFactory();
	    	 ServiceTicketValidator ticketValidator = ticketValidatorFactory.getServiceTicketValidator(sessionTicket);
	    	 ticketValidator.validate();
	    	 isValidated = ticketValidator.isAuthenticationSuccesful();
	     }
	     
	     if(!isValidated && !request.getRequestURI().contains("login")){
    		 response.sendRedirect("loginFailed");
    		 return false;
    	 }
	     
        return super.preHandle(request, response, handler);
    }
	
}
