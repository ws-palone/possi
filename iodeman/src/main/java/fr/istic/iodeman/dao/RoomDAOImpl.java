package fr.istic.iodeman.dao;

import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import fr.istic.iodeman.model.Room;

@Repository
public class RoomDAOImpl extends AbstractHibernateDAO implements RoomDAO {

	public void persist(Room room) {
		Session session = getCurrentSession();
		session.beginTransaction();
		getCurrentSession().save(room);
		session.getTransaction().commit();
	}

	public Room findById(int id) {
		Room room = (Room) getCurrentSession().load(Room.class, id);
		return room; 
	}

	public void delete(Room entity) {
		Session session = getCurrentSession();
		session.beginTransaction();
		session.delete(entity);
		session.getTransaction().commit();
	}

	@SuppressWarnings("unchecked")
	public List<Room> findAll() {
		Session session = getCurrentSession();
		return (List<Room>) session.createCriteria(Room.class).list();
	}

	public void deleteAll() {
		List<Room> entityList = findAll();
		for (Room entity : entityList) {
			delete(entity);
		}
	}
}
