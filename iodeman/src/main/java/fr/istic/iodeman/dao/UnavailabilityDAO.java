package fr.istic.iodeman.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import fr.istic.iodeman.model.TimeBox;
import fr.istic.iodeman.model.Unavailability;

public interface UnavailabilityDAO {
	
	public Session openCurrentSession();

	public Session openCurrentSessionwithTransaction();

	public void closeCurrentSession();

	public void closeCurrentSessionwithTransaction();

	SessionFactory getSessionFactory();

	public Session getCurrentSession();

	public void setCurrentSession(Session currentSession);

	public Transaction getCurrentTransaction();
	
	public void setCurrentTransaction(Transaction currentTransaction);
	
	public void persist(Unavailability u);

	public void update(Unavailability u) ;
	
	public Unavailability findById(int id);

	public void delete(Unavailability u) ;

	@SuppressWarnings("unchecked")
	public List<Unavailability> findAll();

	public void deleteAll();

}
