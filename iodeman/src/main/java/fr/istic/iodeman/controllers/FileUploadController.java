package fr.istic.iodeman.controllers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import fr.istic.iodeman.dto.ExtractParticipantResponseDTO;
import fr.istic.iodeman.services.OralDefenseService;
import org.joda.time.DateTime;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import fr.istic.iodeman.services.PlanningService;

@RestController
public class FileUploadController {

	final
	PlanningService planningService;

	final
	OralDefenseService oralDefenseService;
	

	public FileUploadController(PlanningService planningService, OralDefenseService oralDefenseService) {
		this.planningService = planningService;
		this.oralDefenseService = oralDefenseService;
	}

	@PostMapping(value="/upload/participants")
	public ExtractParticipantResponseDTO uploadParticipants(@RequestParam("file") MultipartFile inputFile) throws Exception {
		String name = "/tmp/"+new DateTime();
		File outputFile = new File(name);

		if (!inputFile.isEmpty()) {
			try {
				byte[] bytes = inputFile.getBytes();
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(outputFile));
				stream.write(bytes);
				stream.close();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		return oralDefenseService.extractParticipantFromCSV(outputFile);
	}

}
