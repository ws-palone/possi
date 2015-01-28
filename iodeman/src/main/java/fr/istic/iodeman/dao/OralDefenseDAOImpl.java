package fr.istic.iodeman.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import fr.istic.iodeman.model.OralDefense;
import fr.istic.iodeman.utils.HibernateUtil;

@Repository
public class OralDefenseDAOImpl extends AbstractHibernateDAO implements OralDefenseDAO {

	public void persist(OralDefense oral) {
		Session session = getNewSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.persist(oral);
			session.getTransaction().commit();	
		} catch (Exception e){
			if (transaction!=null) transaction.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public void delete(OralDefense entity) {
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
	public List<OralDefense> findAll() {
		Session session = getNewSession();
		List<OralDefense> ods = session.createCriteria(OralDefense.class).list();
		session.close();
		return ods;
	}

	public void deleteAll() {
		List<OralDefense> entityList = findAll();
		for (OralDefense entity : entityList) {
			delete(entity);
		}
	}

	public OralDefense findById(int id) {
		Session session = getNewSession();
		OralDefense oralDefense = (OralDefense)session.get(OralDefense.class, id);
		session.close();
		return oralDefense;
	}

}