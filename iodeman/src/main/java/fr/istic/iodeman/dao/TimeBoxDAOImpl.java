package fr.istic.iodeman.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import fr.istic.iodeman.model.TimeBox;

public class TimeBoxDAOImpl {
	private Session currentSession;
	private Transaction currentTransaction;

	public TimeBoxDAOImpl() {

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

	private  SessionFactory getSessionFactory() {
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

	public void persist(TimeBox t) {
		getCurrentSession().save(t);
	}

	public void update(TimeBox t) {
		getCurrentSession().update(t);
	}

	public TimeBox findById(int id) {
		TimeBox timebox = (TimeBox) getCurrentSession().get(TimeBox.class, id);
		return timebox; 
	}

	public void delete(TimeBox t) {
		getCurrentSession().delete(t);
	}

	@SuppressWarnings("unchecked")
	public List<TimeBox> findAll() {
		List<TimeBox> listTimeBox = (List<TimeBox>) getCurrentSession().createQuery("from TimeBox").list();
		return listTimeBox;
	}

	public void deleteAll() {
		List<TimeBox> listTimeBox = findAll();
		for (TimeBox t : listTimeBox) {
			delete(t);
		}
	}
}
