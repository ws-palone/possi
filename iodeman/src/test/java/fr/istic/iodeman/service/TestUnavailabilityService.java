package fr.istic.iodeman.service;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fr.istic.iodeman.dao.PersonDAO;
import fr.istic.iodeman.dao.PlanningDAO;
import fr.istic.iodeman.dao.UnavailabilityDAO;
import fr.istic.iodeman.model.Person;
import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.TimeBox;
import fr.istic.iodeman.model.Unavailability;
import fr.istic.iodeman.resolver.PersonUidResolver;

public class TestUnavailabilityService {
	
	@InjectMocks
	private UnavailabilityService unavailabilityService;
	
	@Mock
	private UnavailabilityDAO unavailabilityDAO;
	
	@Mock
	private PersonUidResolver personUidResolver;
	
	@Mock
	private PersonDAO personDAO;
	
	@Mock
	private PlanningDAO planningDAO;
	
	@Before
	public void setUp() {
		unavailabilityService = new UnavailabilityServiceImpl();
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testCreate() {
		
		Person p1 = new Person();p1.setUid("11008880");
		Person p2 = new Person();p2.setUid("dCertain");
		
		Mockito.when(personUidResolver.resolve("dCertain")).thenReturn(p2);
		
		Planning planning = new Planning();
		planning.setId(777);
		
		Mockito.when(planningDAO.findById(planning.getId())).thenReturn(planning);
		
		TimeBox tb = new TimeBox();
		tb.setFrom(new DateTime(2015,1,10,8,0).toDate());
		tb.setTo(new DateTime(2015,1,14,8,0).toDate());
		
		Unavailability unavailability = unavailabilityService.create(planning.getId(), "dCertain", tb);
		
		ArgumentCaptor<Unavailability> argumentU = ArgumentCaptor.forClass(Unavailability.class);
		Mockito.verify(unavailabilityDAO).persist(argumentU.capture());
		
		Unavailability u = argumentU.getValue();
		
		assertEquals(u.getPerson(), p2);
		assertEquals(u.getId(), unavailability.getId());
		assertEquals(u.getPlanning().getId(), unavailability.getPlanning().getId(), 777);
		
	}
}
