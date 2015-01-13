package fr.istic.iodeman;

import static org.junit.Assert.*;


import org.junit.Test;

import edu.yale.its.tp.cas.client.ServiceTicketValidator;
import edu.yale.its.tp.cas.client.filter.CASFilter;

public class TestConnection {
	
	@Test
	public void sendRequest(){
		String serverName = "https://sso-cas.univ-rennes1.fr/login?service=http://iode-man.istic.univ-rennes1.fr:8080/iodeman/";
		assert(true);
	}
}
