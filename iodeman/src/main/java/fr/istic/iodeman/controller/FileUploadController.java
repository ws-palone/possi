package fr.istic.iodeman.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.service.PlanningService;

@Controller
public class FileUploadController {

	@Autowired
	PlanningService planningService;
	
    @SuppressWarnings("finally")
	@RequestMapping(value="/upload", method=RequestMethod.POST)
    public String handleFileUpload(@RequestParam("planningId") int planningId, @RequestParam("file") MultipartFile inputFile, @RequestParam(value="redirectURL", required=false) String redirectURL){    	
    	// path to save the input file
    	String name = "/tmp/"+new DateTime();
    	
    	// output file
    	File outputFile = new File(name);
    	
    	if (!inputFile.isEmpty()) {
            try {
                byte[] bytes = inputFile.getBytes();
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(outputFile));
                stream.write(bytes);
                stream.close();
//                result =  "Participants ajoutés" + name + "!";
            } catch (Exception e) {
//            	result =  "Erreur lors du téléchargement du fichier " + name + " => " + e.getMessage();
            	e.printStackTrace();
            }
        } else {
//        	result =  "Erreur car votre fichier " + name + " est vide";
        }
    	
    	// on l'envoie au service
    	Planning planning = planningService.findById(new Integer(planningId));
    	try {
			Planning filledPlanning = planningService.importPartcipants(planning, outputFile);
		} catch (Exception e) {
//			result =  "Erreur lors de l'import des participants : ";
			e.printStackTrace();
			// affcher erreur de format
		} finally {
	    	// on supprime le fichier
	    	outputFile.delete();
	    	
	    	return "redirect:"+redirectURL;
		}

    }

}