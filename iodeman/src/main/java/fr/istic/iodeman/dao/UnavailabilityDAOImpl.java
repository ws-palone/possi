package fr.istic.iodeman.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import fr.istic.iodeman.model.TimeBox;
import fr.istic.iodeman.model.Unavailability;
import fr.istic.iodeman.utils.HibernateUtil;

public class UnavailabilityDAOImpl {

	private Session currentSession;
	private Transaction currentTransaction;

	public UnavailabilityDAOImpl() {

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

	public void persist(Unavailability u) {
		getCurrentSession().save(u);
	}

	public void update(Unavailability u) {
		getCurrentSession().update(u);
	}

	public Unavailability findById(int id) {
		Unavailability unavailability = (Unavailability) getCurrentSession().get(Unavailability.class, id);
		return unavailability; 
	}

	public void delete(Unavailability u) {
		getCurrentSession().delete(u);
	}

	@SuppressWarnings("unchecked")
	public List<Unavailability> findAll() {
		List<Unavailability> listUnavailability = (List<Unavailability>) getCurrentSession().createQuery("from Unavailability").list();
		return listUnavailability;
	}

	public void deleteAll() {
		List<Unavailability> listUnavailability = findAll();
		for (Unavailability u : listUnavailability) {
			delete(u);
		}
	}
}
