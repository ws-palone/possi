package fr.istic.iodeman.service;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;

import fr.istic.iodeman.dao.PlanningDAO;
import fr.istic.iodeman.model.Person;
import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.Room;
import fr.istic.iodeman.model.TimeBox;

public class TestPlanningService {

	@InjectMocks
	private PlanningService planningService;
	
	@Mock
	private PlanningDAO planningDAO;
	
	@Before
	public void setUp() {
		
		planningService = new PlanningServiceImpl();
		MockitoAnnotations.initMocks(this);
	
	}
	
	@Test
	public void testCreate() {
		
		String name = "test";
		TimeBox period = new TimeBox(
				new DateTime(2015,1,14,0,0).toDate(), 
				new DateTime(2015,1,21,0,0).toDate()
		);
		Integer oralDefenseDuration = 30;
		Integer oralDefenseInterlude = 5;
		TimeBox lunchBreak = new TimeBox(
				new DateTime(2015,1,14,12,15).toDate(), 
				new DateTime(2015,1,14,14,0).toDate()
		);
		TimeBox dayPeriod = new TimeBox(
				new DateTime(2015,1,14,8,0).toDate(), 
				new DateTime(2015,1,14,18,15).toDate()
		);
		Integer nbMaxOralDefensePerDay = 10;
		Room room1 = new Room();
		room1.setName("i50");
		Room room2 = new Room();
		room2.setName("i60");
		
		Person admin = new Person();
		admin.setUid("dcertain");
		
		Collection<Room> rooms = Lists.newArrayList(room1, room2);
		
		planningService.create(admin, name, period, oralDefenseDuration, oralDefenseInterlude, lunchBreak, dayPeriod, nbMaxOralDefensePerDay, rooms);
		
		ArgumentCaptor<Planning> argument = ArgumentCaptor.forClass(Planning.class);
		Mockito.verify(planningDAO).persist(argument.capture());
		
		Planning p = argument.getValue();
		assertEquals(admin, p.getAdmin());
		assertEquals(name, p.getName());
		assertEquals(period, p.getPeriod());
		assertEquals(oralDefenseDuration, p.getOralDefenseDuration());
		assertEquals(oralDefenseInterlude, p.getOralDefenseInterlude());
		assertEquals(lunchBreak, p.getLunchBreak());
		assertEquals(dayPeriod, p.getDayPeriod());
		assertEquals(nbMaxOralDefensePerDay, p.getNbMaxOralDefensePerDay());
		assertEquals(rooms.size(), p.getRooms().size());
		
	}
	
	@Test
	public void testUpdate() {
		
		Planning planning = new Planning();
		planning.setName("empty");
		planning.setPeriod(new TimeBox(
				new DateTime(2015,1,12,0,0).toDate(), 
				new DateTime(2015,1,23,0,0).toDate()
		));
		
		String name = "test";
		TimeBox period = new TimeBox(
				new DateTime(2015,1,14,0,0).toDate(), 
				new DateTime(2015,1,21,0,0).toDate()
		);
		Integer oralDefenseDuration = 30;
		Integer oralDefenseInterlude = 5;
		TimeBox lunchBreak = new TimeBox(
				new DateTime(2015,1,14,12,15).toDate(), 
				new DateTime(2015,1,14,14,0).toDate()
		);
		TimeBox dayPeriod = new TimeBox(
				new DateTime(2015,1,14,8,0).toDate(), 
				new DateTime(2015,1,14,18,15).toDate()
		);
		Integer nbMaxOralDefensePerDay = 10;
		Room room1 = new Room();
		room1.setName("i50");
		Room room2 = new Room();
		room2.setName("i60");
		
		Collection<Room> rooms = Lists.newArrayList(room1, room2);
		
		planningService.update(planning, name, period, oralDefenseDuration, oralDefenseInterlude, lunchBreak, dayPeriod, nbMaxOralDefensePerDay, rooms);
		
		ArgumentCaptor<Planning> argument = ArgumentCaptor.forClass(Planning.class);
		Mockito.verify(planningDAO).update(argument.capture());
		
		Planning p = argument.getValue();
		assertEquals(name, p.getName());
		assertEquals(period, p.getPeriod());
		assertEquals(oralDefenseDuration, p.getOralDefenseDuration());
		assertEquals(oralDefenseInterlude, p.getOralDefenseInterlude());
		assertEquals(lunchBreak, p.getLunchBreak());
		assertEquals(dayPeriod, p.getDayPeriod());
		assertEquals(nbMaxOralDefensePerDay, p.getNbMaxOralDefensePerDay());
		assertEquals(rooms.size(), p.getRooms().size());
		
	}
	
}
