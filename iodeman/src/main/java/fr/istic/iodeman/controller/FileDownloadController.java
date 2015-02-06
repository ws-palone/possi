package fr.istic.iodeman.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.istic.iodeman.service.PlanningService;

@Controller
public class FileDownloadController {
	
	@Autowired
	PlanningService planningService;
	
	@RequestMapping(value="/planning/{planningId}/export")
	public void downloadPlanning(@PathVariable("planningId") Integer planningId, HttpServletResponse response) throws IOException{
		// retrieving of the generating file containing the agenda
		File file = planningService.exportExcel(planningId);		
		
		// mime type
		response.setContentType("application/vnd.ms-excel");
		
		// name of the returned file
		String filename = "planningExport.xls";
		
		// header
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
        		filename);
        response.setHeader(headerKey, headerValue);
	}
	
}