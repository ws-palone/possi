package fr.istic.iodeman.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Iterator;

import org.joda.time.DateTime;
import org.junit.Test;

import com.google.common.collect.Lists;

import fr.istic.iodeman.model.Participant;
import fr.istic.iodeman.model.Person;
import fr.istic.iodeman.model.Priority;
import fr.istic.iodeman.model.Role;
import fr.istic.iodeman.model.TimeBox;
import fr.istic.iodeman.model.Unavailability;

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
	
	@Test
	public void testExtractUnavailabilities() {
		
		Person p1 = new Person();
		p1.setUid("cclement");
		p1.setRole(Role.STUDENT);
		
		Person p2 = new Person();
		p2.setUid("dcertain");
		p2.setRole(Role.PROF);
		
		Person p3 = new Person();
		p3.setUid("sferre");
		p3.setRole(Role.PROF);
		
		Unavailability ua1 = new Unavailability();
		ua1.setPerson(p1);
		ua1.setPeriod(new TimeBox(
				(new DateTime(2015,1,13,8,30)).toDate(),
				(new DateTime(2015,1,13,9,0)).toDate()
		));
		
		Unavailability ua2 = new Unavailability();
		ua2.setPerson(p2);
		ua2.setPeriod(new TimeBox(
				(new DateTime(2015,1,13,8,30)).toDate(),
				(new DateTime(2015,1,13,9,0)).toDate()
		));
		
		Unavailability ua3 = new Unavailability();
		ua3.setPerson(p2);
		ua3.setPeriod(new TimeBox(
				(new DateTime(2015,1,13,8,30)).toDate(),
				(new DateTime(2015,1,13,9,0)).toDate()
		));
		
		Participant participant = new Participant();
		participant.setStudent(p1);
		participant.setFollowingTeacher(p2);
		
		Collection<Unavailability> unavailabilities = Lists.newArrayList(ua1, ua2, ua3);
		
		Collection<Unavailability> results = AlgoPlanningUtils.extractUnavailabilities(unavailabilities, participant, Role.STUDENT);
		assertEquals(1, results.size());
		
		results = AlgoPlanningUtils.extractUnavailabilities(unavailabilities, participant, Role.PROF);
		assertEquals(2, results.size());
		
	}
}
