package fr.istic.iodeman.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import fr.istic.iodeman.model.Participant;
import fr.istic.iodeman.utils.HibernateUtil;

@Repository
public class ParticipantDAOImpl extends AbstractHibernateDAO implements ParticipantDAO{

	public void persist(Participant par) {
		Session session = getNewSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.persist(par);
			session.getTransaction().commit();	
		} catch (Exception e){
			if (transaction!=null) transaction.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public void delete(Participant entity) {
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
	public List<Participant> findAll() {
		Session session = getNewSession();
		List<Participant> participants = session.createCriteria(Participant.class).list();
		session.close();
		return participants;
	}

	public void deleteAll() {
		List<Participant> entityList = findAll();
		for (Participant entity : entityList) {
			delete(entity);
		}
	}

	public Participant findById(int id) {
		Session session = getNewSession();
		Participant participant = (Participant)session.get(Participant.class, id);
		session.close();
		return participant;
    }

}
