package fr.istic.iodeman.strategy;

import java.io.File;
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
				
		// until we have finished parsing the file
		while(!sheet.getCell(0, i).getContents().equals("")){
			// Student
			/*Person student = new Person();
			student.setLastName(sheet.getCell(1, i).getContents());
			student.setFirstName(sheet.getCell(2, i).getContents());	
			*/
			String fullName = sheet.getCell(2, i).getContents() 
					+ " " + sheet.getCell(1, i).getContents();
			Person student = personResolver.resolve(fullName);
			
			// following teacher
			/*Person followingTeacher = new Person();
			followingTeacher.setFirstName(sheet.getCell(4, i).getContents());
			*/
			fullName = sheet.getCell(4, i).getContents();
			Person followingTeacher = personResolver.resolve(fullName);
			
			Participant participant = new Participant();
			participant.setStudent(student);
			participant.setFollowingTeacher(followingTeacher);
			
			participants.add(participant);
			
			i++;
		}
		
		return participants;
	}

}
