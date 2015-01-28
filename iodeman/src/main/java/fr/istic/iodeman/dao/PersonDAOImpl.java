package fr.istic.iodeman.dao;


import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import fr.istic.iodeman.model.Person;
import fr.istic.iodeman.utils.HibernateUtil;

@Repository
public class PersonDAOImpl extends AbstractHibernateDAO implements PersonDAO {
		
	public void delete(Person entity) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		session.delete(entity);
		session.getTransaction().commit();
	}

	@SuppressWarnings("unchecked")
	public List<Person> findAll() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Person> person = (List<Person>) session.createCriteria(Person.class).list();
		return person;
	}

	public void deleteAll() {
		List<Person> entityList = findAll();
		for (Person entity : entityList) {
			delete(entity);
		}
	}
	
	@SuppressWarnings("unchecked")
	public Person findByUid(String uid) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		List<Person> results = (List<Person>) session.createCriteria(Person.class)
				.add(Restrictions.eq("uid", uid))
				.setFirstResult(0)
				.setMaxResults(1)
				.list();
		Person person = null;
		if (results != null && results.size() > 0) {
			person = results.get(0);
		}
		return person;
	}

	public void persist(Person person) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		session.save(person);
		session.getTransaction().commit();
	}

	public Person findByNames(String names) {
		Session session =  HibernateUtil.getSessionFactory().openSession();
		
		Person person = (Person) session.createCriteria(Person.class)
				.add(Restrictions.ilike("fullName", names.toLowerCase() + "%"))
				.list().get(0);
		return person;
	}
	
}


