package fr.istic.iodeman.strategy;

import java.io.File;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import fr.istic.iodeman.model.Participant;
import fr.istic.iodeman.model.Person;
import fr.istic.iodeman.resolver.PersonResolver;

public class ParticipantsExcelImport implements ParticipantsImport {

	private PersonResolver personResolver;

	public void configure(PersonResolver personResolver) {
		this.personResolver = personResolver;
	}
	
	public Collection<Participant> execute(File file) throws Exception {
		Collection<Participant> participants = new ArrayList<Participant>();
		
		// init excel
		WorkbookSettings settings = new WorkbookSettings();
		settings.setEncoding("Cp1252");
		settings.setSuppressWarnings(true);
		Workbook workbook = Workbook.getWorkbook(file, settings);		
		Sheet sheet = workbook.getSheet(0);
		
		// line index
		int i = 1;	
		
		int numberOfRows = sheet.getRows();
				
		// until we have finished parsing the file
		while(i < numberOfRows){
			
			// quick'n dirty | indicates the end of the array...
			if (sheet.getCell(0, i).getContents().length() == 0) break; 
			
			// create participants
			Participant participant = new Participant();
			
			// Student
			String email = sheet.getCell(1, i).getContents().trim();
			String normedEmail = normalize(email);
			Person student = personResolver.resolve(normedEmail);
			
			// following teacher
			email = sheet.getCell(2, i).getContents().trim();
			normedEmail = normalize(email);
			Person followingTeacher = personResolver.resolve(normedEmail);
			
			// optional. if is an internship oral defense
			// Tutor
			if (sheet.getCell(3, i).getContents() != null) {
				String tutorFullName = sheet.getCell(3, i).getContents();
				
				participant.setTutorFullName(tutorFullName);
				// Compagny
				if (sheet.getCell(4, i).getContents() != null) {
					String company = sheet.getCell(4, i).getContents();
					participant.setCompany(company);
				}
			}
			
			participant.setStudent(student);
			participant.setFollowingTeacher(followingTeacher);
			
			participants.add(participant);

			// update the line index
			i++;
		}
		
		return participants;
	}
	
	private String normalize(String input){
		return Normalizer.normalize(input, Normalizer.Form.NFC).replaceAll("[^\\p{ASCII}]", "");
	}

}
