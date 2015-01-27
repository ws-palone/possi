package fr.istic.iodeman.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.istic.iodeman.model.Person;

@RestController
public class PersonController {
	
	@RequestMapping("/user")
	public String user(HttpSession session){

		return session.getAttribute("uid").toString();
		
	}

}
