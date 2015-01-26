package fr.istic.iodeman;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.istic.iodeman.model.Person;
import fr.istic.iodeman.service.LdapRepositoryImpl;


@RestController  
public class TestController {  
	
	public class Greeting {
		
		private String name;
		
		public Greeting(String name) {
			setName(name);
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
	}
	
	@Autowired
	private LdapRepositoryImpl ldapserv;
	
	@RequestMapping("/greeting") 
	public Greeting sayHello(@RequestParam(value="name", defaultValue="World") String name) {  
		
		return new Greeting(name);
		
	}
	
	@RequestMapping("/ldap/{uid}")
	public Person ldapUID(@PathVariable("uid") String uid){
		
	    return (Person) ldapserv.searchByUID(uid);
		
	}
	
	@RequestMapping("/ldap")
	public Person ldap(@RequestParam(value="name", defaultValue="") String name){
		
	    return (Person) ldapserv.lookupPerson(name);
		
	}

	
}  
