package fr.istic.iodeman.controller;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorController {
	
	@RequestMapping("/loginFailed")
	public String loginFailed(HttpSession session){
		String ticket = (String) session.getAttribute("cas_ticket");
		String uid = (String) session.getAttribute("uid");
		return "Error no logs : "+ticket+" ("+uid+")";
	}

}
