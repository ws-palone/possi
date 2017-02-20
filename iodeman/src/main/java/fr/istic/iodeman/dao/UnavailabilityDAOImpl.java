package fr.istic.iodeman.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.istic.iodeman.model.Person;
import fr.istic.possijar.Creneau;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
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
		
		criteria.add(Restrictions.and(
				Restrictions.eq("pers.uid", uid),
				Restrictions.eq("plan.id", idPlanning)
		));
		
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		unavailabilities = criteria.list();
		session.close();
		return unavailabilities;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Unavailability> findByPlanningId(Integer idPlanning) {
		Session session = getNewSession();
		List<Unavailability> unavailabilities = new ArrayList<Unavailability>();
		Criteria criteria = session.createCriteria(Unavailability.class);
		criteria.createAlias("planning", "planning");
		criteria.add(Restrictions.eq("planning.id", idPlanning));

		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		unavailabilities = criteria.list();
		session.close();
		return unavailabilities;
	}

	@Override
	public void deleteByPlanning(Integer planningId) {
		// TODO quick n dirty
		for(Unavailability u : findByPlanningId(planningId)){
			delete(u);
		}
	}
	

	@Override
	public void deleteAll(Integer id, Integer ref_id) {
		Session session = getNewSession();
		String query = "DELETE FROM Unavailability WHERE person_id = :id and planning_id = :ref_id";

		SQLQuery sqlQuery = session.createSQLQuery(query);
		sqlQuery.setParameter("id", id);
		sqlQuery.setParameter("ref_id", ref_id);
		sqlQuery.executeUpdate();
		session.close();
	}

	@Override
	public List<Date> getUnavailabilities(Integer planning_ref_Id, Creneau creneau) {
		Session session = getNewSession();
		Criteria student = session.createCriteria(Person.class);
		student.add(Restrictions.eq("email",  creneau.getStudent().getName()));
		Person s = (Person) student.uniqueResult();

		Criteria prof = session.createCriteria(Person.class);
		prof.add(Restrictions.eq("email",  creneau.getEnseignant().getName()));
		Person p = (Person) prof.uniqueResult();

		Criteria criteria = session.createCriteria(Unavailability.class);

		criteria.setProjection(Projections.property("period.from"));

		criteria.add(Restrictions.eq("planning.id",planning_ref_Id));
		criteria.add(Restrictions.or(
						Restrictions.eq("person.id", s.getId()),
						Restrictions.eq("person.id", p.getId())
				));
		criteria.setProjection(Projections.groupProperty("period.from"));

		List<Date> ids = criteria.list();
		session.close();

		return ids;
	}

}
