package fr.istic.iodeman.utils;

import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;

import com.google.common.collect.Lists;

import fr.istic.iodeman.model.Priority;
import fr.istic.iodeman.model.Role;
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
	
}
