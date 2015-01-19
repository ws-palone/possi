package fr.istic.iodeman.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import fr.istic.iodeman.model.Person;
import fr.istic.iodeman.utils.HibernateUtil;

public class DaoPerson {
	private Session session;
	private Transaction tx;

	public void createPerson(Person p){
		session = HibernateUtil.getSessionFactory().openSession();
		//start transaction
		tx = session.beginTransaction();
		//Save the Model object
		session.save(p);
		//Commit transaction
		tx.commit();
		session.close();
	}

	// Retourne la liste de tous les Users présents dans la base de données
	public List<Person> findAllPerson() {
		session = HibernateUtil.getSessionFactory().openSession();
		List<Person> list = session.createQuery("from person p").list();

		session.close();

		return list;

	}
	
	 public void main(String[] args){
		 
		 List<Person> listp= new ArrayList();
		 Iterator it = Iterato

		 
	 }
}
