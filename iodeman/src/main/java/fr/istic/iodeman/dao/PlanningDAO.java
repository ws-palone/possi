package fr.istic.iodeman.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import fr.istic.iodeman.model.Participant;
import fr.istic.iodeman.model.Planning;

public interface PlanningDAO {

	public List<Planning> findAll();
	
	 public void persist(Planning pla);
		
		public void update(Planning pla);
		
		public Planning findById(int ID);
		
		public void delete(Planning pla);
		
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
