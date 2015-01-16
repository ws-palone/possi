package fr.istic.iodeman.utils;

import java.util.Collection;
import java.util.Iterator;

import org.joda.time.DateTime;
import org.junit.Test;

import com.google.common.collect.Lists;

import fr.istic.iodeman.model.Priority;
import fr.istic.iodeman.model.Role;
import fr.istic.iodeman.model.TimeBox;
import fr.istic.iodeman.model.Unavailability;
import static org.junit.Assert.*;

public class AlgoPlanningUtilsTest {

	@Test
	public void testSortingPriorities() {
		
		Priority priority1 = new Priority();
		priority1.setRole(Role.STUDENT);
		priority1.setWeight(1);
		
		Priority priority2 = new Priority();
		priority2.setWeight(10);
		priority2.setRole(Role.PROF);
		
		Collection<Priority> priorities = AlgoPlanningUtils.sortPrioritiesByWeight(Lists.newArrayList(priority1, priority2));
		
		Iterator<Priority> it = priorities.iterator();
		
		assertEquals(priority2, it.next());
		assertEquals(priority1, it.next());
		
	}
	
	@Test
	public void testSortingPrioritiesWithDoublons() {
		
		Priority priority1 = new Priority();
		priority1.setRole(Role.STUDENT);
		priority1.setWeight(1);
		
		Priority priority2 = new Priority();
		priority2.setWeight(1);
		priority2.setRole(Role.PROF);
		
		Collection<Priority> priorities = AlgoPlanningUtils.sortPrioritiesByWeight(Lists.newArrayList(priority1, priority2));
		
		assertEquals(2, priorities.size());
		assertTrue(priorities.contains(priority1));
		assertTrue(priorities.contains(priority2));
		
		for(Priority p : priorities) {
			System.out.println(p.getRole().toString()+" - "+p.getWeight());
		}
		
	}
	
	@Test
	public void testIsAvailable1() {
		
		Unavailability ua = new Unavailability();
		ua.setPeriod(new TimeBox(
				(new DateTime(2015,1,13,8,0)).toDate(),
				(new DateTime(2015,1,13,9,0)).toDate()
		));
		
		TimeBox timeBox = new TimeBox(
				(new DateTime(2015,1,13,8,15)).toDate(),
				(new DateTime(2015,1,13,8,30)).toDate()
		);
		
		assertTrue(!AlgoPlanningUtils.isAvailable(ua, timeBox));
	}
	
	@Test
	public void testIsAvailable2() {
		
		Unavailability ua = new Unavailability();
		ua.setPeriod(new TimeBox(
				(new DateTime(2015,1,13,8,30)).toDate(),
				(new DateTime(2015,1,13,9,0)).toDate()
		));
		
		TimeBox timeBox = new TimeBox(
				(new DateTime(2015,1,13,8,0)).toDate(),
				(new DateTime(2015,1,13,8,30)).toDate()
		);
		
		assertTrue(AlgoPlanningUtils.isAvailable(ua, timeBox));
	}
	
	@Test
	public void testIsAvailable3() {
		
		Unavailability ua = new Unavailability();
		ua.setPeriod(new TimeBox(
				(new DateTime(2015,1,13,8,30)).toDate(),
				(new DateTime(2015,1,13,9,0)).toDate()
		));
		
		TimeBox timeBox = new TimeBox(
				(new DateTime(2015,1,13,8,0)).toDate(),
				(new DateTime(2015,1,13,9,0)).toDate()
		);
		
		assertTrue(!AlgoPlanningUtils.isAvailable(ua, timeBox));
	}
	
	@Test
	public void testIsAvailable4() {
		
		Unavailability ua = new Unavailability();
		ua.setPeriod(new TimeBox(
				(new DateTime(2015,1,13,8,30)).toDate(),
				(new DateTime(2015,1,13,9,0)).toDate()
		));
		
		TimeBox timeBox = new TimeBox(
				(new DateTime(2015,1,13,8,45)).toDate(),
				(new DateTime(2015,1,13,10,0)).toDate()
		);
		
		assertTrue(!AlgoPlanningUtils.isAvailable(ua, timeBox));
	}
	
	@Test
	public void testIsAvailable5() {
		
		Unavailability ua = new Unavailability();
		ua.setPeriod(new TimeBox(
				(new DateTime(2015,1,13,8,30)).toDate(),
				(new DateTime(2015,1,13,9,0)).toDate()
		));
		
		TimeBox timeBox = new TimeBox(
				(new DateTime(2015,1,13,8,0)).toDate(),
				(new DateTime(2015,1,13,10,0)).toDate()
		);
		
		assertTrue(!AlgoPlanningUtils.isAvailable(ua, timeBox));
	}
	
	@Test
	public void testIsAvailable6() {
		
		Unavailability ua = new Unavailability();
		ua.setPeriod(new TimeBox(
				(new DateTime(2015,1,13,8,30)).toDate(),
				(new DateTime(2015,1,13,9,0)).toDate()
		));
		
		TimeBox timeBox = new TimeBox(
				(new DateTime(2015,1,13,8,15)).toDate(),
				(new DateTime(2015,1,13,8,45)).toDate()
		);
		
		assertTrue(!AlgoPlanningUtils.isAvailable(ua, timeBox));
	}
	
	@Test
	public void testIsAvailable7() {
		
		Unavailability ua = new Unavailability();
		ua.setPeriod(new TimeBox(
				(new DateTime(2015,1,13,8,30)).toDate(),
				(new DateTime(2015,1,13,9,0)).toDate()
		));
		
		TimeBox timeBox = new TimeBox(
				(new DateTime(2015,1,13,8,30)).toDate(),
				(new DateTime(2015,1,13,9,0)).toDate()
		);
		
		assertTrue(!AlgoPlanningUtils.isAvailable(ua, timeBox));
	}
	
}
