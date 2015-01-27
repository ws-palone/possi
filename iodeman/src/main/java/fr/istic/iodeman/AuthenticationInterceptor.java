package fr.istic.iodeman;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {

        // set few parameters to handle ajax request from different host
        /*response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        response.addHeader("Access-Control-Max-Age", "1000");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type");
        response.addHeader("Cache-Control", "private");*/

        //String reqUri = request.getRequestURI();
        //String serviceName = reqUri.substring(reqUri.lastIndexOf("/") + 1, reqUri.length());
     
    	if (!request.getRequestURI().contains("login")) {
    		response.sendRedirect("/login");
    		return false;
    	}
    	
        return super.preHandle(request, response, handler);
    }
	
}
