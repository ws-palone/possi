package fr.istic.iodeman.strategy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;

import fr.istic.iodeman.model.Participant;
import fr.istic.iodeman.model.Person;
import fr.istic.iodeman.model.Role;
import fr.istic.iodeman.resolver.PersonResolver;

public class ParticipantsCSVImport implements ParticipantsImport {

	private PersonResolver personResolver;

	public void configure(PersonResolver personResolver) {
		this.personResolver = personResolver;
	}
	
	public Collection<Participant> execute(File file) throws Exception {
		
		Collection<Participant> participants = new ArrayList<Participant>();
		Collection<String> students = new ArrayList<String>();
		
		BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";
        
        System.err.println("debut");
        
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



				if (!emailStudent.equals(emailTeacher) && student.getRole().equals(Role.STUDENT) && followingTeacher.getRole().equals(Role.PROF) && !students.contains(student.getUid()) && !participants.contains(participant)) {

					// Student
					System.err.println(emailStudent);
					System.err.println(normedEmailStudent);

					if (student == null) {
						student = new Person();
						student.setEmail(normedEmailStudent);
					}


					System.err.println("1");

					// following teacher
					if (followingTeacher == null) {
						followingTeacher = new Person();
						followingTeacher.setEmail(normedEmailTeacher);
					}

					String tutorFullName = row[5].trim();

					System.err.println("2");

					participant.setTutorFullName(tutorFullName);
					// Compagny
					String company = row[3].trim();
					participant.setCompany(company);

					participant.setStudent(student);
					participant.setFollowingTeacher(followingTeacher);

					students.add(student.getUid());
					participants.add(participant);

					System.err.println("3");
				}else{
					System.err.println("Email étudiant et professeur identiques.");
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
        
        System.err.println("fin");
		
        return participants;
	}
	
	private String normalize(String input){
		return Normalizer.normalize(input, Normalizer.Form.NFC).replaceAll("[^\\p{ASCII}]", "");
	}

}
