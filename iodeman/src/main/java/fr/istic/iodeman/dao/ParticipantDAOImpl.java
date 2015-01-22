package fr.istic.iodeman.dao;

import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Service;

import fr.istic.iodeman.model.Participant;

@Service
public class ParticipantDAOImpl extends AbstractHibernateDAO implements ParticipantDAO{

	public void persist(Participant par) {
		Session session = getCurrentSession();
		session.beginTransaction();
		getCurrentSession().save(par);
		session.getTransaction().commit();
	}

	public void delete(Participant entity) {
		Session session = getCurrentSession();
		session.beginTransaction();
		session.delete(entity);
		session.getTransaction().commit();
	}

	@SuppressWarnings("unchecked")
	public List<Participant> findAll() {
		return getCurrentSession().createCriteria(Participant.class).list();
	}

	public void deleteAll() {
		List<Participant> entityList = findAll();
		for (Participant entity : entityList) {
			delete(entity);
		}
	}

	public Participant findById(int id) {
		Participant par = (Participant) getCurrentSession().get(Participant.class, id);
		return par;
    }

}
