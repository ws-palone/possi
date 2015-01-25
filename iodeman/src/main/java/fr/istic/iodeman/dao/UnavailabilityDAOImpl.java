package fr.istic.iodeman.dao;

import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import fr.istic.iodeman.model.Unavailability;

@Repository
public class UnavailabilityDAOImpl extends AbstractHibernateDAO implements UnavailabilityDAO {

	public void persist(Unavailability unav) {
		Session session = getCurrentSession();
		session.beginTransaction();
		getCurrentSession().save(unav);
		session.getTransaction().commit();
	}

	public Unavailability findById(Integer id) {
		Unavailability unav = (Unavailability) getCurrentSession().load(Unavailability.class, id);
		return unav; 
	}

	public void delete(Unavailability entity) {
		Session session = getCurrentSession();
		session.beginTransaction();
		session.delete(entity);
		session.getTransaction().commit();
	}

	@SuppressWarnings("unchecked")
	public List<Unavailability> findAll() {
		Session session = getCurrentSession();
		return (List<Unavailability>) session.createCriteria(Unavailability.class).list();
	}

	public void deleteAll() {
		List<Unavailability> entityList = findAll();
		for (Unavailability entity : entityList) {
			delete(entity);
		}
	}

}
