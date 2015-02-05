package fr.istic.iodeman.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.Priority;

@Repository
public class PriorityDAOImpl extends AbstractHibernateDAO implements PriorityDAO {

	public void persist(Priority priority) {
		Session session = getNewSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.persist(priority);
			session.getTransaction().commit();	
		} catch (Exception e){
			if (transaction!=null) transaction.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	public void update(Priority priority) {
		Session session = getNewSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.update(priority);
			session.getTransaction().commit();	
		} catch (Exception e){
			if (transaction!=null) transaction.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public Priority findById(int id) {
		Session session = getNewSession();
		Priority priority = (Priority)session.get(Priority.class, id);
		session.close();
		return priority;
	}

	public void delete(Priority entity) {
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
	public List<Priority> findAll() {
		Session session = getNewSession();
		List<Priority> priority = session.createCriteria(Priority.class).list();
		session.close();
		return priority;
	}

	public void deleteAll() {
		List<Priority> entityList = findAll();
		for (Priority entity : entityList) {
			delete(entity);
		}
	}
}
