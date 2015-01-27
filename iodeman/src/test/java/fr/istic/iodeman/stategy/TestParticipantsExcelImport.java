package fr.istic.iodeman.stategy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

import fr.istic.iodeman.model.Participant;
import fr.istic.iodeman.model.Person;
import fr.istic.iodeman.resolver.PersonResolver;
import fr.istic.iodeman.strategy.ParticipantsExcelImport;
import fr.istic.iodeman.strategy.ParticipantsImport;

public class TestParticipantsExcelImport {
	
	private class PersonResolverMock implements PersonResolver {

		public Person resolve(String name) {
			Person person = new Person();
			person.setFirstName(name);
			return person;
		}
		
	}
	
	@Test
	public void testExecute() throws Exception{
		ParticipantsImport particpantsImport = new ParticipantsExcelImport();
		String filename = "/Listing_2004_M2_MIAGE_Stages_Encadrants.xls";

		File excelFile = new File(getClass().getResource(filename).toURI());
		assertTrue(excelFile.exists());
		
		particpantsImport.configure(new PersonResolverMock());
		List<Participant> participants = Lists.newArrayList(particpantsImport.execute(excelFile));

		assertTrue(participants != null);
		assertTrue(participants.size() > 0);
		assertTrue(participants.size() == 49);

		// first one
		Participant firstOne = participants.get(0);
		assertEquals(firstOne.getStudent().getFirstName(), "13008385");
		assertEquals(firstOne.getFollowingTeacher().getFirstName(), "dcertain");
		
		//Accent
		Participant encodageOne = participants.get(11);
		assertEquals(encodageOne.getStudent().getFirstName(), "13008396");
		assertEquals(encodageOne.getFollowingTeacher().getFirstName(), "mbousse");
		
		// lastone
		Participant lastOne = participants.get(48);
		assertEquals(lastOne.getStudent().getFirstName(), "13008433");
		assertEquals(lastOne.getFollowingTeacher().getFirstName(), "glesventes");
		
		
	}
}
