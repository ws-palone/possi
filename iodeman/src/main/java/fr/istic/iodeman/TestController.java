package fr.istic.iodeman;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.istic.iodeman.service.LDAPServ;


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
	private LDAPServ ldapserv;
	
	@RequestMapping("/greeting") 
	public Greeting sayHello(@RequestParam(value="name", defaultValue="World") String name) {  
		
		return new Greeting(name);
		
	}
	
	@RequestMapping("/hello")
	public String hello(@RequestParam(value="name", defaultValue="") String name){
		
		/*List<String> list = ldapserv.getAllPersonNames();
		
		String listString = "List : ";

		for (String s : list)
		{
		    listString += s + "\t";
		}

	    return listString;*/
	    
	    return (String) ldapserv.lookupPerson(name);
		
	}
}  
