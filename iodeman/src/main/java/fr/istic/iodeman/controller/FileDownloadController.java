package fr.istic.iodeman.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import fr.istic.iodeman.SessionComponent;
import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.service.PlanningService;

@Controller
public class FileDownloadController {
	
	@Autowired
	PlanningService planningService;
	
	@Autowired
	private SessionComponent session;
	
	@RequestMapping(value="/planning/{planningId}/export")
	public void downloadPlanning(@PathVariable("planningId") Integer planningId, HttpServletResponse response) throws IOException{
		
		// retrieve planning
		Planning planning = planningService.findById(planningId);
		Validate.notNull(planning);
		
		// check if the current user is the admin of this planning
		session.acceptOnly(planning.getAdmin());
		
		// mime type
		//response.setContentType("application/vnd.ms-excel");
		
		// name of the returned file
		String filename = "planningExport.csv";
		
		// header
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
        		filename);
        response.setHeader(headerKey, headerValue);
        
		// retrieving of the generating file containing the agenda
		File file = planningService.exportExcel(planning);		
		
		// BEGIN write the file
		FileInputStream in = new FileInputStream(file);
		OutputStream out = response.getOutputStream();

		byte[] buffer= new byte[8192]; 
		int length = 0;

		while ((length = in.read(buffer)) > 0){
		     out.write(buffer, 0, length);
		}
		in.close();
		out.close();
		// END write the file
		
		file.delete();
	}
	
	@RequestMapping(value="/planning/{planningId}/exportPlanning", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String showPlanning(@PathVariable("planningId") Integer planningId, HttpServletResponse response) throws IOException{
		Planning planning = planningService.findById(planningId);
		Validate.notNull(planning);
		
		File file = planningService.exportExcel(planning);	
		
		//return Files.toString(file, Charsets.UTF_8);
		return "{\"file\":1}";
	}
	
}