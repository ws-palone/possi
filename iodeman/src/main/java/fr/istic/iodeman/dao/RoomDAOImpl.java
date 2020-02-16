package fr.istic.iodeman.dao;

import fr.istic.iodeman.model.Room;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoomDAOImpl extends AbstractHibernateDAO implements RoomDAO {

	public void persist(Room room) {
		Session session = getNewSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.persist(room);
			session.getTransaction().commit();	
		} catch (Exception e){
			if (transaction!=null) transaction.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	// FIXME: 16/02/2020 créer des rooms en partant d'une liste
	@Override
	public void persit(List<String> names) {
		Session session = getNewSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			names.forEach(n ->{
				session.persist(n);
				session.getTransaction().commit();
			});
		} catch (Exception e){
			if (transaction!=null) transaction.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public Room findById(int id) {
		Session session = getNewSession();
		Room room = (Room)session.get(Room.class, id);
		session.close();
		return room;
	}

	public void delete(Room entity) {
		Session session = getNewSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.delete(entity);
			session.getTransaction().commit();	
		} catch (Exception e){
			if (transaction!=null) transaction.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Room> findAll() {
		Session session = getNewSession();
		List<Room> rooms = session.createCriteria(Room.class).list();
		session.close();
		return rooms;
	}

	public void deleteAll() {
		List<Room> entityList = findAll();
		for (Room entity : entityList) {
			delete(entity);
		}
	}

	public Room findByName(String name) {
		Session session = getNewSession();
		Room room =  (Room) session.createCriteria(Room.class)
				.add(Restrictions.eq("name", name))
				.uniqueResult();
		session.close();
		return room;
	}
}
