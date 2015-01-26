package fr.istic.iodeman.stategy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

import fr.istic.iodeman.model.Participant;
import fr.istic.iodeman.strategy.ParticipantsExcelImport;
import fr.istic.iodeman.strategy.ParticipantsImport;

public class TestParticipantsExcelImport {
	@Test
	public void testExecute() throws Exception{
		ParticipantsImport particpantsImport = new ParticipantsExcelImport();
		String filename = "/Listing_2004_M2_MIAGE_Stages_Encadrants.xls";

		File excelFile = new File(getClass().getResource(filename).toURI());
		assertTrue(excelFile.exists());
		
		List<Participant> participants = Lists.newArrayList(particpantsImport.execute(excelFile));

		assertTrue(participants != null);
		assertTrue(participants.size() > 0);
		assertTrue(participants.size() == 49);

		// first one
		Participant firstOne = participants.get(0);
		assertEquals(firstOne.getStudent().getFirstName(), "Antoine");
		assertEquals(firstOne.getStudent().getLastName(), "AMELINE");
		assertEquals(firstOne.getFollowingTeacher().getFirstName(), "Didier Certain");
		
		//Accent
		Participant encodageOne = participants.get(11);
		assertEquals(encodageOne.getStudent().getFirstName(), "Yaëlle");
		assertEquals(encodageOne.getStudent().getLastName(), "ECHIVARD");
		assertEquals(encodageOne.getFollowingTeacher().getFirstName(), "Marc Bousse");
		
		// lastone
		Participant lastOne = participants.get(48);
		assertEquals(lastOne.getStudent().getFirstName(), "Guillaume");
		assertEquals(lastOne.getStudent().getLastName(), "YAN");
		assertEquals(lastOne.getFollowingTeacher().getFirstName(), "Gilles Lesventes");
		
		
	}
}
