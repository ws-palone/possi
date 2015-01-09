package fr.istic.iodeman;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController  
public class TestController {  
	
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
	public Greeting sayHello(@RequestParam(value="name", defaultValue="World") String name) {  
		Greeting g = new Greeting();
		g.setName(name);
		return g;
	}
	
}  
