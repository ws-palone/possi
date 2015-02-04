package fr.istic.iodeman.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import fr.istic.iodeman.model.Unavailability;

@Repository
public class UnavailabilityDAOImpl extends AbstractHibernateDAO implements UnavailabilityDAO {

	public void persist(Unavailability unav) {
		Session session = getNewSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.persist(unav);
			session.getTransaction().commit();	
		} catch (Exception e){
			if (transaction!=null) transaction.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public Unavailability findById(Integer id) {
		Session session = getNewSession();
		Unavailability unavailability = (Unavailability)session.get(Unavailability.class, id);
		session.close();
		return unavailability;
	}

	public void delete(Unavailability entity) {
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
	public List<Unavailability> findAll() {
		Session session = getNewSession();
		List<Unavailability> unavailabilities = session.createCriteria(Unavailability.class).list();
		session.close();
		return unavailabilities;
	}

	public void deleteAll() {
		List<Unavailability> entityList = findAll();
		for (Unavailability entity : entityList) {
			delete(entity);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Unavailability> findById(Integer idPlanning, String uid) {
		Session session = getNewSession();
		List<Unavailability> unavailabilities = new ArrayList<Unavailability>();
		Criteria criteria = session.createCriteria(Unavailability.class);
		criteria.createAlias("planning", "plan");
		criteria.createAlias("person", "pers");	
		
		criteria.add(Restrictions.eq("pers.uid", uid));
		criteria.add(Restrictions.eq("plan.id", idPlanning));
		
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		unavailabilities = criteria.list();
		session.close();
		return unavailabilities;
	}

}
