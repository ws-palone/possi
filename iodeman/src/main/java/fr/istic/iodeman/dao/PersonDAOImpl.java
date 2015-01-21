package fr.istic.iodeman.dao;


import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import fr.istic.iodeman.model.Person;

public class PersonDAOImpl implements PersonDAO {
	private Session currentSession;
	private Transaction currentTransaction;

	public PersonDAOImpl() {

	}
	public Session openCurrentSession() {
		currentSession = getSessionFactory().openSession();
		return currentSession;
	}

	public Session openCurrentSessionwithTransaction() {
		currentSession = getSessionFactory().openSession();
		currentTransaction = currentSession.beginTransaction();
		return currentSession;
	}

	public void closeCurrentSession() {
		currentSession.close();
	}

	public void closeCurrentSessionwithTransaction() {
		currentTransaction.commit();
		currentSession.close();
	}

	private static SessionFactory getSessionFactory() {
		Configuration configuration = new Configuration().configure();
		StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
		.applySettings(configuration.getProperties());
		SessionFactory sessionFactory = configuration.buildSessionFactory(builder.build());
		return sessionFactory;
	}

	public Session getCurrentSession() {
		return currentSession;
	}

	public void setCurrentSession(Session currentSession) {
		this.currentSession = currentSession;
	}

	public Transaction getCurrentTransaction() {
		return currentTransaction;
	}

	public void setCurrentTransaction(Transaction currentTransaction) {
		this.currentTransaction = currentTransaction;
	}

	public void persist(Person person) {
		getCurrentSession().save(person);
	}

	public void update(Person person) {
		getCurrentSession().update(person);
	}

	public Person findById(String id) {
		Person person = (Person) getCurrentSession().get(Person.class, id);
		return person; 
	}

	public void delete(Person entity) {
		getCurrentSession().delete(entity);
	}

	@SuppressWarnings("unchecked")
	public List<Person> findAll() {
		List<Person> person = (List<Person>) getCurrentSession().createQuery("from Person").list();
		return person;
	}

	public void deleteAll() {
		List<Person> entityList = findAll();
		for (Person entity : entityList) {
			delete(entity);
		}
	}

	public Person findById(int Id) {

		Person person = (Person) getCurrentSession().get(Person.class, Id);
		return person;
	}

}