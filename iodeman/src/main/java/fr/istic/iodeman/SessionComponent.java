package fr.istic.iodeman;

import java.io.IOException;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import fr.istic.iodeman.model.Person;
import fr.istic.iodeman.model.Role;
import fr.istic.iodeman.resolver.PersonUidResolver;

@Component
public class SessionComponent {

	@Autowired
	private PersonUidResolver personResolver;
	
	public HttpSession getSession() {
		
	    ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	    return attr.getRequest().getSession(true); // true == allow create
	}
	
	public void teacherOnly(){
		ServletWebRequest servletWebRequest = (ServletWebRequest) RequestContextHolder.getRequestAttributes();
		HttpServletResponse httpServletResponse = servletWebRequest.getResponse();
		
		if(getUser().getRole() != Role.PROF){
			try {
				httpServletResponse.sendError(403);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public String getUserUID() {
		
		return (String) getSession().getAttribute("uid");
	}
	
	public Person getUser() {
		
		String uid = getUserUID();
		if (uid != null && !uid.equals("")) {
			return personResolver.resolve(uid);
		}
		return null;
	}
	
}
