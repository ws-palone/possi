package fr.istic.iodeman.dao;


import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.springframework.transaction.annotation.Transactional;

import fr.istic.iodeman.model.OralDefense;
import fr.istic.iodeman.model.Person;

 
public class OralDefenseDAOImpl implements OralDefenseDAO {
    
	private Session currentSession;
	private Transaction currentTransaction;


	public OralDefenseDAOImpl() {

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

	public void persist(OralDefense oral) {
		getCurrentSession().save(oral);
	}

	public void update(OralDefense oral) {
		getCurrentSession().update(oral);
	}

	public OralDefense findById(String id) {
		OralDefense oral = (OralDefense) getCurrentSession().get(OralDefense.class, id);
		return oral; 
	}

	public void delete(OralDefense entity) {
		getCurrentSession().delete(entity);
	}

	@SuppressWarnings("unchecked")
	public List<OralDefense> findAll() {
		List<OralDefense> oral = (List<OralDefense>) getCurrentSession().createQuery("from oraldefense").list();
		return oral;
	}

	public void deleteAll() {
		List<OralDefense> entityList = findAll();
		for (OralDefense entity : entityList) {
			delete(entity);
		}
	}

	@Override
	public OralDefense findById(int Id) {

		OralDefense oral = (OralDefense) getCurrentSession().get(OralDefense.class, Id);

		return oral;

    }
 
}