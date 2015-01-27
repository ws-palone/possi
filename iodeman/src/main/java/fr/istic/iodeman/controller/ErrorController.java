package fr.istic.iodeman.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorController {
	
	@RequestMapping("/loginFailed")
	public String loginFailed(){
		return "Error no logs";
	}

}
