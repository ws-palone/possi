package fr.istic.iodeman.dao;

import java.util.List;



import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import fr.istic.iodeman.model.Person;

public interface PersonDAO {
    
    public void persist(Person person);
	
	public void update(Person person);
	
	public Person findById(int ID);
	
	public void delete(Person person);
	
	public List<Person> findAll();
	
	public void deleteAll();
	
	public Session openCurrentSession();

	public Session openCurrentSessionwithTransaction() ;
	
	public void closeCurrentSession() ;
	
	public void closeCurrentSessionwithTransaction();


	public Session getCurrentSession();

	public void setCurrentSession(Session currentSession);
	public Transaction getCurrentTransaction();

	public void setCurrentTransaction(Transaction currentTransaction);
	

}