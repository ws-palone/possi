package fr.istic.iodeman.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginFailedController {
	
	@RequestMapping("login_failed")
	public String LoginFailed(){
		return "Error no logs";
	}

}
