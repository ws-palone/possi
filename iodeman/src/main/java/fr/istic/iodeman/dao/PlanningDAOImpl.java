package fr.istic.iodeman.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Lists;

import fr.istic.iodeman.model.Participant;
import fr.istic.iodeman.model.Planning;

@Repository
public class PlanningDAOImpl extends AbstractHibernateDAO implements PlanningDAO {
	
	public void persist(Planning planning) {
		Session session = getNewSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.persist(planning);
			session.getTransaction().commit();	
		} catch (Exception e){
			if (transaction!=null) transaction.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	
	public void update(Planning planning) {
		Session session = getNewSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.update(planning);
			session.getTransaction().commit();	
		} catch (Exception e){
			if (transaction!=null) transaction.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public Planning findById(Integer id) {
		Session session = getNewSession();
		Planning planning = (Planning)session.get(Planning.class, id);
		session.close();
		return planning;
	}

	public void delete(Planning entity) {
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
	public List<Planning> findAll() {
		Session session = getNewSession();
		List<Planning> plannings = session.createCriteria(Planning.class).list();
		session.close();
		return plannings;
	}
	
	public List<Planning> findAll(String uid) {
		Session session = getNewSession();
		List<Planning> plannings = new ArrayList<Planning>();
		Criteria criteria = session.createCriteria(Planning.class);
		criteria.createAlias("admin", "adm");
		criteria.createAlias("participants", "parts");
		criteria.createAlias("parts.student", "student");
		criteria.createAlias("parts.followingTeacher", "teacher");
		
		
		criteria.add(Restrictions.or(
				Restrictions.eq("student.uid", uid),
				Restrictions.eq("teacher.uid", uid),
				Restrictions.eq("adm.uid", uid)
				));
		
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		plannings = criteria.list();
		session.close();
		return plannings;
	}

	public void deleteAll() {
		List<Planning> entityList = findAll();
		for (Planning entity : entityList) {
			delete(entity);
		}
	}
	
	public Collection<Participant> findParticipants(Planning planning) {
		Session session = getNewSession();
		Collection<Participant> participants = null;
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			participants = Lists.newArrayList(planning.getParticipants());
			session.getTransaction().commit();	
		} catch (Exception e){
			if (transaction!=null) transaction.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return participants;
	}

}
