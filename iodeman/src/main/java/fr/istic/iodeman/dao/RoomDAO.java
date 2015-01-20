package fr.istic.iodeman.dao;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import fr.istic.iodeman.model.Room;

public interface RoomDAO {

	public Session openCurrentSession();

	public Session openCurrentSessionwithTransaction();

	public void closeCurrentSession();

	public void closeCurrentSessionwithTransaction();

	SessionFactory getSessionFactory();

	public Session getCurrentSession();

	public void setCurrentSession(Session currentSession);

	public Transaction getCurrentTransaction();
	
	public void setCurrentTransaction(Transaction currentTransaction);
	
	public void persist(Room r);

	public void update(Room r) ;
	
	public Room findById(int id);

	public void delete(Room r) ;

	@SuppressWarnings("unchecked")
	public List<Room> findAll();

	public void deleteAll();
	
}
