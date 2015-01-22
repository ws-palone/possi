package fr.istic.iodeman.dao;

import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Service;

import fr.istic.iodeman.model.TimeBox;

@Service
public class TimeBoxDAOImpl extends AbstractHibernateDAO implements TimeBoxDAO {
	
	public void persist(TimeBox time) {
		Session session = getCurrentSession();
		session.beginTransaction();
		getCurrentSession().save(time);
		session.getTransaction().commit();
	}

	public TimeBox findById(Integer id) {
		TimeBox time = (TimeBox) getCurrentSession().load(TimeBox.class, id);
		return time; 
	}

	public void delete(TimeBox entity) {
		Session session = getCurrentSession();
		session.beginTransaction();
		session.delete(entity);
		session.getTransaction().commit();
	}

	@SuppressWarnings("unchecked")
	public List<TimeBox> findAll() {
		Session session = getCurrentSession();
		return (List<TimeBox>) session.createCriteria(TimeBox.class).list();
	}

	public void deleteAll() {
		List<TimeBox> entityList = findAll();
		for (TimeBox entity : entityList) {
			delete(entity);
		}
	}

}
