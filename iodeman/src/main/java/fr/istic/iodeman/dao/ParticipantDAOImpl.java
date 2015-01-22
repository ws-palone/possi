package fr.istic.iodeman.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import fr.istic.iodeman.model.Participant;

public class ParticipantDAOImpl implements ParticipantDAO{
	
	private Session currentSession;
	private Transaction currentTransaction;


	public ParticipantDAOImpl() {

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

	public void persist(Participant par) {
		getCurrentSession().save(par);
	}

	public void update(Participant par) {
		getCurrentSession().update(par);
	}

	public Participant findById(String id) {
		Participant par = (Participant) getCurrentSession().get(Participant.class, id);
		return par; 
	}

	public void delete(Participant entity) {
		getCurrentSession().delete(entity);
	}

	@SuppressWarnings("unchecked")
	public List<Participant> findAll() {
		List<Participant> par = (List<Participant>) getCurrentSession().createQuery("from participant").list();
		return par;
	}

	public void deleteAll() {
		List<Participant> entityList = findAll();
		for (Participant entity : entityList) {
			delete(entity);
		}
	}

	@Override
	public Participant findById(int Id) {

		Participant par = (Participant) getCurrentSession().get(Participant.class, Id);

		return par;

    }

}
