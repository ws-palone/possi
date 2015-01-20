package fr.istic.iodeman.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import fr.istic.iodeman.model.OralDefense;
import fr.istic.iodeman.model.Participant;

public interface ParticipantDAO {
	
	public List<Participant> findAll();
	
	 public void persist(Participant par);
		
		public void update(Participant par);
		
		public Participant findById(int ID);
		
		public void delete(Participant par);
		
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
