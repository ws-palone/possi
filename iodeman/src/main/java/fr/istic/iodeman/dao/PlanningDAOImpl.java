package fr.istic.iodeman.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.istic.iodeman.model.Participant;
import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.utils.HibernateUtil;

@Service
public class PlanningDAOImpl implements PlanningDAO {
	
	public PlanningDAOImpl() {

	}
	
	private Session getCurrentSession() {
		if (HibernateUtil.getSessionFactory().isClosed()) {
			return HibernateUtil.getSessionFactory().openSession();
		}
		return HibernateUtil.getSessionFactory().getCurrentSession();
	}

	public void persist(Planning planning) {
		Session session = getCurrentSession();
		session.beginTransaction();
		getCurrentSession().save(planning);
		session.getTransaction().commit();
	}

	public void update(Planning pla) {
		getCurrentSession().update(pla);
	}

	public Planning findById(Integer id) {
		Planning pla = (Planning) getCurrentSession().load(Planning.class, id);
		return pla; 
	}

	public void delete(Planning entity) {
		Session session = getCurrentSession();
		session.beginTransaction();
		session.delete(entity);
		session.getTransaction().commit();
	}

	@SuppressWarnings("unchecked")
	public List<Planning> findAll() {
		Session session = getCurrentSession();
		return (List<Planning>) session.createCriteria(Planning.class).list();
	}

	public void deleteAll() {
		List<Planning> entityList = findAll();
		for (Planning entity : entityList) {
			delete(entity);
		}
	}

}
