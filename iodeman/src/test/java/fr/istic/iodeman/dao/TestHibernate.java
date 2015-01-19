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

	@Test
	public void test() {
		
		Person per = new Person();
		per.setFirstName("Pankaj");
		per.setLastName("blabla");
		per.setRole(Role.STUDENT);
		//Get Session
		Session session = HibernateUtil.getSessionFactory().openSession();
		//start transaction
		session.beginTransaction();
		//Save the Model object
		System.out.println(per.getId());
		session.save(per);
		System.out.println(per.getId());
		//Commit transaction
		session.getTransaction().commit();
		//terminate session factory, otherwise program won't end
		
		
		
		
		HibernateUtil.getSessionFactory().close();
		
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
		EntityManager em = emf.createEntityManager();
		Person personDB = em.find(Person.class, per.getId());
		
		/*assertTrue(per.getId().equals(personDB.getId()));
		assertTrue(per.getFirstName().equals(personDB.getFirstName()));
		assertTrue(per.getLastName().equals(personDB.getLastName()));
		assertTrue(per.getRole().equals(personDB.getRole()));*/
		
	}


}
