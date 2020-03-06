package fr.istic.iodeman.strategy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;

import fr.istic.iodeman.error.ErrorImport;
import fr.istic.iodeman.model.Participant;
import fr.istic.iodeman.model.Person;
import fr.istic.iodeman.model.Role;
import fr.istic.iodeman.resolver.PersonResolver;

public class ParticipantsCSVImport implements ParticipantsImport {

	private PersonResolver personResolver;

	private Collection<ErrorImport> errorsImport = new ArrayList<ErrorImport>();

	public void configure(PersonResolver personResolver) {
		this.personResolver = personResolver;
	}
	
	public Collection<Participant> execute(File file) throws Exception {
		
		Collection<Participant> participants = new ArrayList<Participant>();
		Collection<String> students = new ArrayList<String>();
		
		BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";
        int lineNumber = 0;
        
        System.err.println("Start Import");
        
        try {
            br = new BufferedReader(new FileReader(file));
            line = br.readLine();
            if(!line.contains(",")) {
            	cvsSplitBy = ";";
            }
            if(!line.contains(";")) {
            	cvsSplitBy = ",";
            }
            while ((line = br.readLine()) != null) {
            	lineNumber ++;
            	System.err.println(line);
                // use comma as separator
                String[] row = line.split(cvsSplitBy);
                System.err.println(row[1].trim() + " " + row[2].trim());
                String stu_nom = row[1].trim();
                String stu_prenom = row[2].trim();
                
                // create participants
    			Participant participant = new Participant();
				String emailStudent = row[0].trim();
				String emailTeacher = row[4].trim();
				String normedEmailStudent = normalize(emailStudent);
				Person student = personResolver.resolve(normedEmailStudent);
				String normedEmailTeacher = normalize(emailTeacher);
				Person followingTeacher = personResolver.resolve(normedEmailTeacher);

				// Student
				System.err.println(emailStudent);
				System.err.println(normedEmailStudent);
				if (student == null) {
					student = new Person();
					student.setEmail(normedEmailStudent);
				}

				// following teacher
				if (followingTeacher == null) {
					followingTeacher = new Person();
					followingTeacher.setEmail(normedEmailTeacher);
				}
				String tutorFullName = row[5].trim();
				participant.setTutorFullName(tutorFullName);
				// Compagny
				String company = row[3].trim();
				participant.setCompany(company);
				participant.setStudent(student);
				participant.setFollowingTeacher(followingTeacher);

				if (emailStudent.equals(emailTeacher)){
					ErrorImport error = new ErrorImport("Ligne " + lineNumber + " : "  + participant.toString(), "Ligne " + lineNumber + " du CSV : "  + "L'étudiant et le professeur référent ont le même mail. (doublon)");
					errorsImport.add(error);
				}
				else if (!student.getRole().equals(Role.STUDENT)){
					ErrorImport error = new ErrorImport("Ligne " + lineNumber + " : "  + participant.toString(), "Ligne " + lineNumber + " du CSV : "  + "La personne renseigné dans le champ étudiant n'est pas un étudiant reconnu.");
					errorsImport.add(error);
				}
				else if (!followingTeacher.getRole().equals(Role.PROF)){
					ErrorImport error = new ErrorImport("Ligne " + lineNumber + " : "  + participant.toString(), "Ligne " + lineNumber + " du CSV : "  + "La personne renseigné dans le champ professeur référent n'est pas un professeur reconnu");
					errorsImport.add(error);
				}
				else if (students.contains(student.getUid())){
					ErrorImport error = new ErrorImport("Ligne " + lineNumber + " : "  + participant.toString(), "Ligne " + lineNumber + " du CSV : "  + "L'étudiant est déjà présent dans cet import.");
					errorsImport.add(error);
				}
				else if (participants.contains(participant)) {
					ErrorImport error = new ErrorImport("Ligne " + lineNumber + " : "  + participant.toString(), "Ligne " + lineNumber + " du CSV : "  + "La soutenance existe déjà dans cet import.");
					errorsImport.add(error);
				}
				else {
					students.add(student.getUid());
					participants.add(participant);
				}
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.err.println("end import");
        return participants;
	}
	
	private String normalize(String input){
		return Normalizer.normalize(input, Normalizer.Form.NFC).replaceAll("[^\\p{ASCII}]", "");
	}

	public Collection<ErrorImport> getErrorsImport() {
		return errorsImport;
	}

}
