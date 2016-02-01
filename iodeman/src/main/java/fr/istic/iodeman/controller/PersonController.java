package fr.istic.iodeman.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.istic.iodeman.SessionComponent;
import fr.istic.iodeman.model.Person;
import fr.istic.iodeman.resolver.PersonMailResolver;
import fr.istic.iodeman.resolver.PersonUidResolver;
import fr.istic.iodeman.service.LdapRepository;

@RestController
public class PersonController {
	
	@Autowired
	private SessionComponent session;
	
	@Autowired
	PersonUidResolver resolverUID;
	
	@Autowired
	PersonMailResolver resolverMail;
	
	@Autowired
	private LdapRepository ldap;
	
	@RequestMapping("/user")
	public Person user(){
		System.err.println("Current user " + session.getUser());
		return session.getUser();
		
	}
	
	@RequestMapping("/person/{uid}")
	public Person ldapUID(@PathVariable("uid") String uid){
		
	    return resolverUID.resolve(uid);
		
	}
	
	@RequestMapping("/person")
	public Person ldap(@RequestParam(value="mail", defaultValue="") String mail){
		
		if (!mail.equals("")) {
			return resolverMail.resolve(mail);
		}
		
	    return null;
	}

}
