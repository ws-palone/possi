package fr.istic.iodeman.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.istic.iodeman.model.Person;
import fr.istic.iodeman.resolver.PersonMailResolver;
import fr.istic.iodeman.resolver.PersonUidResolver;
import fr.istic.iodeman.service.LdapRepository;

@RestController
public class PersonController {
	
	@Autowired
	PersonUidResolver resolverUID;
	
	@Autowired
	PersonMailResolver resolverMail;
	
	@Autowired
	private LdapRepository ldap;
	
	@RequestMapping("/user")
	public Person user(HttpSession session){
		
		String uid = session.getAttribute("uid").toString();
		return resolverUID.resolve(uid);
		
	}
	
	@RequestMapping("/person/{uid}")
	public Person ldapUID(@PathVariable("uid") String uid){
		
	    return (Person) ldap.searchByUID(uid);
		
	}
	
	@RequestMapping("/person")
	public Person ldap(@RequestParam(value="mail", defaultValue="") String mail){
		
		if (!mail.equals("")) {
			return resolverMail.resolve(mail);
		}
		
	    return null;
	}

}
