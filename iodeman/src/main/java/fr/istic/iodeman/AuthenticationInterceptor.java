package fr.istic.iodeman;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


public class AuthenticationInterceptor extends HandlerInterceptorAdapter {
	
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {
    	
    	boolean isValidated = false;
    	
    	 HttpSession session = request.getSession();
	     String sessionTicket = (String) session.getAttribute("cas_ticket");
	             
	     if(sessionTicket != null){
	    	 isValidated = true;
	     }
	     
	     if(!isValidated && !request.getContextPath().equals("/") && !request.getContextPath().equals("login")){
    		 response.sendRedirect("loginFailed");
    		 return false;
    	 }
	     
        return super.preHandle(request, response, handler);
    }
	
}
