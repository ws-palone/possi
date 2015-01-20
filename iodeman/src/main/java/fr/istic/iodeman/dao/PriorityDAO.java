package fr.istic.iodeman.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.Priority;

public interface PriorityDAO {
	
	public List<Priority> findAll();
	
	 public void persist(Priority prio);
		
		public void update(Priority prio);
		
		public Priority findById(int ID);
		
		public void delete(Priority prio);
		
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
