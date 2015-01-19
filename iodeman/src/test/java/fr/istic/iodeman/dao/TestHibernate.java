package fr.istic.iodeman.dao;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;

import fr.istic.iodeman.model.Person;
import fr.istic.iodeman.model.Role;
import fr.istic.iodeman.utils.HibernateUtil;

public class TestHibernate {

	/*@Before
	public void init(){

	}*/

	//@Test
	public void test() {
		
		Person per = new Person();
		per.setFirstName("Pankaj");
		per.setLastName("blabla");
		per.setRole(Role.STUDENT);
	
		Session session = HibernateUtil.getSessionFactory().openSession();
		//start transaction
		session.beginTransaction();
		//Save the Model object
		session.save(per);
		//Commit transaction
		session.getTransaction().commit();
		
		Person personDB = (Person) session.load(Person.class, per.getId());
		
		assertTrue(per.getId().equals(personDB.getId()));
		assertTrue(per.getFirstName().equals(personDB.getFirstName()));
		assertTrue(per.getLastName().equals(personDB.getLastName()));
		assertTrue(per.getRole().equals(personDB.getRole()));
		
		HibernateUtil.getSessionFactory().close();
	}
}
