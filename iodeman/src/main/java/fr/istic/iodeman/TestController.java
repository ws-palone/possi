package fr.istic.iodeman;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


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
	
	@RequestMapping("/greeting") 
	public Greeting sayHello(@RequestParam(value="name", defaultValue="World") String name) {  
		
		return new Greeting(name);
		
	}

	@RequestMapping("/test")
	public String test(HttpServletRequest request) {
		return request.getPathTranslated();
	}
	
}  
