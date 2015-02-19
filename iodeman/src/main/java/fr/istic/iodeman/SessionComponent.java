package fr.istic.iodeman;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import fr.istic.iodeman.model.Person;
import fr.istic.iodeman.model.Role;
import fr.istic.iodeman.resolver.PersonUidResolver;

@Component
public class SessionComponent {
	
	@SuppressWarnings("serial")
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	private class PermissionDeniedException extends RuntimeException{
		
	}

	@Autowired
	private PersonUidResolver personResolver;
	
	public void init(String ticket, String uid) {
		getSession().setAttribute("cas_ticket", ticket);
	    getSession().setAttribute("uid", uid); 
	}
	
	public void destroy() {
		getSession().removeAttribute("cas_ticket");
		getSession().removeAttribute("uid");
	}
	
	public HttpSession getSession() {
		
	    ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	    return attr.getRequest().getSession(true); // true == allow create
	}
	
	public void teacherOnly(){
		
		if(getUser().getRole() != Role.PROF){
			throw new PermissionDeniedException();
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
	
	public void acceptOnly(Person p) {
		if (!p.equals(getUser())) {
			throw new PermissionDeniedException();
		}
	}
	
}
