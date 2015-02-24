package fr.istic.iodeman;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


public class AuthenticationInterceptor extends HandlerInterceptorAdapter {
	
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {
    	
    	response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
		response.setHeader("Cache-Control", "no-cache");
    	
    	boolean isValidated = false;
    	
    	 HttpSession session = request.getSession();
	     String sessionTicket = (String) session.getAttribute("cas_ticket");
	             
	     if(sessionTicket != null){
	    	 isValidated = true;
	     }
	     
	     if(!isValidated && !isInWhiteList(request)){
    		 //response.sendRedirect(Application.getURL(request)+"/loginFailed");
	    	 response.sendError(403);
    		 return false;
    	 }
	     
        return super.preHandle(request, response, handler);
    }
    
    private boolean isInWhiteList(HttpServletRequest request) {
    	
    	String path = request.getServletPath();
    	
    	return (
    			path == null 
    			|| path.equals("") 
    			|| path.equals("/") 
    			|| path.contains("login")
    			|| path.contains("logout")
    	);
    	
    }
   
	
}
