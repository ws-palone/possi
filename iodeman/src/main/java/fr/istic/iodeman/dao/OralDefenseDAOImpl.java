package fr.istic.iodeman.dao;

import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Service;

import fr.istic.iodeman.model.OralDefense;

@Service
public class OralDefenseDAOImpl extends AbstractHibernateDAO implements OralDefenseDAO {

	public void persist(OralDefense oral) {
		Session session = getCurrentSession();
		session.beginTransaction();
		getCurrentSession().save(oral);
		session.getTransaction().commit();
	}

	public void delete(OralDefense entity) {
		Session session = getCurrentSession();
		session.beginTransaction();
		session.delete(entity);
		session.getTransaction().commit();
	}

	@SuppressWarnings("unchecked")
	public List<OralDefense> findAll() {
		Session session = getCurrentSession();
		return (List<OralDefense>) session.createCriteria(OralDefense.class).list();
	}

	public void deleteAll() {
		List<OralDefense> entityList = findAll();
		for (OralDefense entity : entityList) {
			delete(entity);
		}
	}

	public OralDefense findById(int id) {
		OralDefense oral = (OralDefense) getCurrentSession().get(OralDefense.class, id);
		return oral; 
	}
 
}