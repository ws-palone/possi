package fr.istic.iodeman.dao;

import com.google.common.collect.Lists;
import fr.istic.iodeman.model.Participant;
import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.Priority;
import fr.istic.iodeman.model.Unavailability;
import org.apache.commons.io.FileUtils;
import org.hibernate.*;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.*;

@Repository
public class PlanningDAOImpl extends AbstractHibernateDAO implements PlanningDAO {

	@Value("${PERSIST_PATH}")
	private String PERSIST_PATH;
	
	public Integer persist(Planning planning) {
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
			return planning.getId();
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
		
		System.err.println("DELETE PLANNING");
		System.err.println(entity);
		System.err.println(entity.getName());
		System.err.println(entity.getAdmin());
		System.err.println(entity.getId());
		Session session = getNewSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.delete(entity);
			session.getTransaction().commit();
			File fileEntry = new File(PERSIST_PATH+"/"+entity.getId());
			FileUtils.deleteDirectory(fileEntry);

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

		criteria.add(Restrictions.eq("is_ref", 1));

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
	public Integer duplicate(Integer id) {
		Session session = getNewSession();

		Date d = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(
				DateFormat.SHORT,
				DateFormat.SHORT
		);

		//inserer une nouvelle ligne dans planing en copiant la reference
		Planning clone = this.findById(id);
		clone.setIs_ref(0);
		clone.setRef_id(id);
		clone.setId(null);
		String name = clone.getName();
		clone.setName(name+ " - Draft " + dateFormat.format(d));
		Integer newId = this.persist(clone);

		// impact sur Planning participant/ Planning Priority/ planning Room
		String sql = "INSERT INTO Unavailability (period_from, period_to, person_id, planning_id) " +
				"SELECT period_from, period_to,person_id, :newid " +
				"FROM Unavailability " +
				"WHERE planning_id = :id";

		SQLQuery query = session.createSQLQuery(sql);
		query.setParameter("id", id);
		query.setParameter("newid", newId);
		query.executeUpdate();

		String sql_room = "INSERT INTO Planning_Room (Planning_id, rooms_id) " +
				"SELECT :newid, rooms_id " +
				"FROM Planning_Room " +
				"WHERE Planning_id = :id";

		SQLQuery query_room = session.createSQLQuery(sql_room);
		query_room.setParameter("id", id);
		query_room.setParameter("newid", newId);
		query_room.executeUpdate();

		/*String sql_priority = "INSERT INTO Planning_Priority (Planning_id, priorities_id) " +
				"SELECT :newid, priorities_id " +
				"FROM Planning_Priority " +
				"WHERE Planning_id = :id";

		SQLQuery query_priority = session.createSQLQuery(sql_priority);
		query_priority.setParameter("id", id);
		query_priority.setParameter("newid", newId);
		query_priority.executeUpdate();*/

		String sql_participant = "INSERT INTO Planning_Participant (Planning_id, participants_id)  " +
				"SELECT :newid, participants_id " +
				"FROM Planning_Participant " +
				"WHERE Planning_id = :id";

		SQLQuery query_participant = session.createSQLQuery(sql_participant);
		query_participant.setParameter("id", id);
		query_participant.setParameter("newid", newId);
		query_participant.executeUpdate();


		session.close();
		return newId;

	}
	@Override
	public Integer duplicateDraft (Integer id){
		Session session = getNewSession();

		Date d = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(
				DateFormat.SHORT,
				DateFormat.SHORT
		);

		//inserer une nouvelle ligne dans planing en copiant la reference
		Planning clone = this.findById(id);
//		clone.setIs_ref(0);
//		clone.setRef_id(id);
		clone.setId(null);
		String name = clone.getName();
		String[] split = name.split("-");
		name = "";
		for(int i=0; i< split.length-1; i++){
			name += split[i] + "-";
		}

		clone.setName(name+ " Draft " + dateFormat.format(d));
		Integer newId = this.persist(clone);

		// impact sur Planning participant/ Planning Priority/ planning Room
		String sql = "INSERT INTO Unavailability (period_from, period_to, person_id, planning_id) " +
				"SELECT period_from, period_to,person_id, :newid " +
				"FROM Unavailability " +
				"WHERE planning_id = :id";

		SQLQuery query = session.createSQLQuery(sql);
		query.setParameter("id", id);
		query.setParameter("newid", newId);
		query.executeUpdate();

		String sql_room = "INSERT INTO Planning_Room (Planning_id, rooms_id) " +
				"SELECT :id, rooms_id " +
				"FROM Planning_Room " +
				"WHERE Planning_id = :newid";

		SQLQuery query_room = session.createSQLQuery(sql_room);
		query_room.setParameter("id", id);
		query_room.setParameter("newid", newId);
		query_room.executeUpdate();

		/*String sql_priority = "INSERT INTO Planning_Priority (Planning_id, priorities_id) " +
				"SELECT :newid, priorities_id " +
				"FROM Planning_Priority " +
				"WHERE Planning_id = :id";

		SQLQuery query_priority = session.createSQLQuery(sql_priority);
		query_priority.setParameter("id", id);
		query_priority.setParameter("newid", newId);
		query_priority.executeUpdate();*/

		String sql_participant = "INSERT INTO Planning_Participant (Planning_id, participants_id)  " +
				"SELECT :id, participants_id " +
				"FROM Planning_Participant " +
				"WHERE Planning_id = :newid";

		SQLQuery query_participant = session.createSQLQuery(sql_participant);
		query_participant.setParameter("id", id);
		query_participant.setParameter("newid", newId);
		query_participant.executeUpdate();
		session.close();
		return newId;

	}
	@Override
	public List<Planning> findDrafts(Integer id) {
		Session session = getNewSession();
		List<Planning> plannings = new ArrayList<Planning>();
		Criteria criteria = session.createCriteria(Planning.class);


		criteria.add(Restrictions.or(
				Restrictions.eq("ref_id", id)
		));

		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		plannings = criteria.list();
		session.close();
		return plannings;

	}

	@Override
	public void switchReference(Integer idDraft) {
		Session session = getNewSession();

		//Au niveau du draft
		Planning draft = this.findById(idDraft);
		Integer id_ref = draft.getRef_id();
		Planning ancienne_ref = this.findById(id_ref);
		draft.setIs_ref(1);
		draft.setRef_id(null);
		draft.setName(ancienne_ref.getName());
		Integer new_id = draft.getId();
		this.update(draft);

		//Autres drafts
		List<Planning> entityList = findDrafts(id_ref);
		for (Planning entity : entityList) {
			entity.setRef_id(new_id);
			this.update(entity);
		}

		//reference actuelle

		Date d = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(
				DateFormat.SHORT,
				DateFormat.SHORT
		);

		ancienne_ref.setIs_ref(0);
		ancienne_ref.setRef_id(new_id);
		ancienne_ref.setName(ancienne_ref.getName() + " - Old " + dateFormat.format(d));
		this.update(ancienne_ref);

		String query_planning = "UPDATE `Unavailability` SET `planning_id`= :new_ref WHERE `planning_id`= :ref_id";
		SQLQuery sql_query_planning = session.createSQLQuery(query_planning);
		sql_query_planning.setParameter("ref_id", id_ref);
		sql_query_planning.setParameter("new_ref", new_id);
		sql_query_planning.executeUpdate();

		session.close();
	}

	@Override
	public void deleteDraft(Integer id) {
		Session session = getNewSession();

		String query_planning = "SELECT id FROM Planning WHERE ref_id= :id";


		SQLQuery sql_query_planning = session.createSQLQuery(query_planning);
		sql_query_planning.setParameter("id", id);
		List ids_list = sql_query_planning.list();
		if(ids_list.size() > 0){

			String ids = "";
			for (int i=0; i< ids_list.size(); i++) {
				if(i != 0){
					ids += ", ";
				}
				ids += ids_list.get(i);

				File fileEntry = new File(PERSIST_PATH + "/"+ids_list.get(i));
				try {
					FileUtils.deleteDirectory(fileEntry);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			System.out.println(ids);

			String query_unavailability = "DELETE FROM Unavailability WHERE planning_id IN ("+ids+")";

			SQLQuery sql_query_unavailability = session.createSQLQuery(query_unavailability);
			sql_query_unavailability.executeUpdate();

			String query_room = "DELETE FROM Planning_Room WHERE planning_id IN ("+ids+")";

			SQLQuery sql_query_room = session.createSQLQuery(query_room);
			sql_query_room.executeUpdate();

			String query_planning_delete = "Delete FROM Planning WHERE ref_id= :id";

			SQLQuery sql_query_planning_delete = session.createSQLQuery(query_planning_delete);
			sql_query_planning_delete.setParameter("id", id);
			sql_query_planning_delete.executeUpdate();

		}
		session.close();
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
			System.err.println(uid + " " + nb);
			map.put(uid, nb);
		}
		
		return map;
	}

}
