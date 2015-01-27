package fr.istic.iodeman.controller;

import java.net.HttpCookie;

import javax.naming.Context;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.CookieGenerator;

import fr.istic.iodeman.model.Person;
import fr.istic.iodeman.resolver.PersonResolver;

@RestController
public class PersonController {
	
	@Autowired
	PersonResolver resolver;
	
	@RequestMapping("/user")
	public Person user(HttpSession session){
		
		String uid = session.getAttribute("uid").toString();
		return resolver.resolve(uid);
		
	}

}
