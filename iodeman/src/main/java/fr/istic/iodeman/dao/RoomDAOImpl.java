package fr.istic.iodeman.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import fr.istic.iodeman.model.Role;
import fr.istic.iodeman.model.Room;
import fr.istic.iodeman.utils.HibernateUtil;

public class RoomDAOImpl {

	private Session currentSession;
	private Transaction currentTransaction;

	public RoomDAOImpl() {

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

	public void persist(Room r) {
		getCurrentSession().save(r);
	}

	public void update(Room r) {
		getCurrentSession().update(r);
	}

	public Room findById(int id) {
		Room room = (Room) getCurrentSession().get(Room.class, id);
		return room; 
	}

	public void delete(Room r) {
		getCurrentSession().delete(r);
	}

	@SuppressWarnings("unchecked")
	public List<Room> findAll() {
		List<Room> listRoom = (List<Room>) getCurrentSession().createQuery("from Room").list();
		return listRoom;
	}

	public void deleteAll() {
		List<Room> listRoom = findAll();
		for (Room r : listRoom) {
			delete(r);
		}
	}
}
