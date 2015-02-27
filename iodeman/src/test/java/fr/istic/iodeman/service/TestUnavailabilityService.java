package fr.istic.iodeman.service;

import static org.junit.Assert.*;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;

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
	
	@Mock
	private MailService MailService;
	
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
	
	@Test
	public void testDelete() {
		
		Person p2 = new Person();p2.setUid("dCertain");
		
		Planning planning = new Planning();
		planning.setAdmin(p2);
		planning.setId(777);
		
		Mockito.when(planningDAO.findById(planning.getId())).thenReturn(planning);
		
		TimeBox tb = new TimeBox();
		tb.setFrom(new DateTime(2015,1,13,9,0).toDate());
		tb.setTo(new DateTime(2015,1,13,11,0).toDate());
		
		List<Unavailability> unavailabilities = Lists.newArrayList();
		
		Unavailability ua1 = new Unavailability();
		ua1.setPeriod(new TimeBox(
				new DateTime(2015,1,13,8,45).toDate(),
				new DateTime(2015,1,13,9,15).toDate()
		));
		unavailabilities.add(ua1);
		
		Unavailability ua2 = new Unavailability();
		ua2.setPeriod(new TimeBox(
				new DateTime(2015,1,13,10,0).toDate(),
				new DateTime(2015,1,13,11,0).toDate()
		));
		unavailabilities.add(ua2);
		
		Unavailability ua3 = new Unavailability();
		ua3.setPeriod(new TimeBox(
				new DateTime(2015,1,13,14,45).toDate(),
				new DateTime(2015,1,13,15,15).toDate()
		));
		unavailabilities.add(ua3);
		
		Mockito.when(unavailabilityDAO.findById(planning.getId(), p2.getUid())).thenReturn(unavailabilities);
		
		unavailabilityService.delete(planning.getId(), p2.getUid(), tb);
		
		Mockito.verify(unavailabilityDAO, Mockito.times(1)).delete(ua1);
		Mockito.verify(unavailabilityDAO, Mockito.times(1)).delete(ua2);
		Mockito.verify(unavailabilityDAO, Mockito.times(0)).delete(ua3);
		
	}
}
