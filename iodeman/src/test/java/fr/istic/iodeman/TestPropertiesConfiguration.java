package fr.istic.iodeman;

import org.apache.commons.lang.Validate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import fr.istic.iodeman.cas.TicketValidatorFactory;
import fr.istic.iodeman.utils.AbstractSpringUnitTest;

public class TestPropertiesConfiguration extends AbstractSpringUnitTest {

	@Value("${IODEMAN_URL}")
	private String serverName;
	
	@Autowired
	private TicketValidatorFactory ticketValidatorFactory;
	
	@Test
	public void testServerNameConfigured() {
		
		Validate.notEmpty(serverName);
		System.out.println("serverName: "+serverName);
	}
	
}
