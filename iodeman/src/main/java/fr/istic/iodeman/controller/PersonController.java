package fr.istic.iodeman.controller;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.istic.iodeman.model.Person;

@RestController
public class PersonController {
	
	@RequestMapping("/user")
	public String user(@CookieValue(value = "CAS TICKET", required=false) String cookie){
		
		
		
		return cookie;
		
	}

}
