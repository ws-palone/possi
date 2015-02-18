package fr.istic.iodeman.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;

import fr.istic.iodeman.model.Participant;
import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.Priority;
import fr.istic.iodeman.model.Unavailability;

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

	@Override
	public Map<String, Integer> findParticipantsAndUnavailabilitiesNumber(
			Planning planning, Collection<String> uids) {
		Session session = getNewSession();
		
//		SELECT Person.uid, count(Unavailability.person_id) as nbUnavailabilities
//		FROM Unavailability, Person 
//		WHERE Unavailability.person_id=Person.id
//		AND planning_id=1
//		AND Person.uid IN ("13008385", "13006294")
//		GROUP BY Unavailability.person_id

		Criteria criteria = session.createCriteria(Unavailability.class);
		
		// create alias for Person table
		criteria.createAlias("person", "person1");
		
		// projections
		criteria.setProjection(Projections.projectionList()
				.add(Property.forName("person1.uid"))
				.add(Projections.rowCount())
				.add(Projections.groupProperty("person1.id").as("person1.id"))
		);
		
		// restrictions
		criteria.add(Restrictions.and(
				Restrictions.eq("planning.id", planning.getId()),
				Restrictions.in("person1.uid", uids)
				)
		);
			
		Iterator it = criteria
				.list()
				.iterator();
		
		session.close();
		
		
		// processing
		Map<String, Integer> map = new HashMap<String, Integer>();	
		
		// 0 : uid
		// 1 : number of unavailabilities
		while(it.hasNext()){
			Object[] tuple = (Object[])it.next();
			String uid = String.valueOf(tuple[0]);
			Integer nb = Integer.valueOf(String.valueOf(tuple[1]));
			map.put(uid, nb);
		}
		
		return map;
	}

}
