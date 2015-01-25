package fr.istic.iodeman.dao;

import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import fr.istic.iodeman.model.Priority;

@Repository
public class PriorityDAOImpl extends AbstractHibernateDAO implements PriorityDAO {

	public void persist(Priority priority) {
		Session session = getCurrentSession();
		session.beginTransaction();
		getCurrentSession().save(priority);
		session.getTransaction().commit();
	}

	public Priority findById(int id) {
		Priority prio = (Priority) getCurrentSession().load(Priority.class, id);
		return prio; 
	}

	public void delete(Priority entity) {
		Session session = getCurrentSession();
		session.beginTransaction();
		session.delete(entity);
		session.getTransaction().commit();
	}

	@SuppressWarnings("unchecked")
	public List<Priority> findAll() {
		Session session = getCurrentSession();
		return (List<Priority>) session.createCriteria(Priority.class).list();
	}

	public void deleteAll() {
		List<Priority> entityList = findAll();
		for (Priority entity : entityList) {
			delete(entity);
		}
	}
}
