package fr.istic.iodeman.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;

import fr.istic.iodeman.model.Participant;
import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.Priority;

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
		criteria.createAlias("participants", "parts", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("parts.student", "student", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("parts.followingTeacher", "teacher", JoinType.LEFT_OUTER_JOIN);
		
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
	

	@SuppressWarnings("unchecked")
	public Collection<Participant> findParticipants(Planning planning) {
		Session session = getNewSession();
		Planning retrievedPlanning = (Planning) session.createCriteria(Planning.class)
				.add(Restrictions.eq("id", planning.getId()))
				.setFetchMode("participants", FetchMode.JOIN)
				.uniqueResult();
		
		if (retrievedPlanning == null) {
			return Lists.newArrayList();
		}
		
		Collection<Participant> participants = Lists.newArrayList(retrievedPlanning.getParticipants());
		session.close();
		return participants;
	}

	@Override
	public Collection<Priority> findPriorities(Planning planning) {
		Session session = getNewSession();
		Planning planningRetrieved = (Planning) session.createCriteria(Planning.class)
				.add(Restrictions.eq("id", planning.getId()))
				.setFetchMode("priotities", FetchMode.JOIN)
				.uniqueResult();
		
		if (planningRetrieved == null) {
			return Lists.newArrayList();
		}
		
		Collection<Priority> priorities = Lists.newArrayList(planningRetrieved.getPriorities());
		session.close();
		return priorities;
	}

}
