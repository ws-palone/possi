package fr.istic.iodeman;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.istic.iodeman.service.LDAPServiceImpl;


@RestController  
public class TestController {  
	
	@Autowired
	public LDAPServiceImpl s;
	
	public class Greeting {
		
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
	}
	
	@RequestMapping("/greeting") 
	public List<String> sayHello(@RequestParam(value="name", defaultValue="World") String name) {  
		
		return s.getAllPersonName();
		
	}
}  
