package fr.istic.iodeman.dao;


import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import fr.istic.iodeman.model.Person;

@Repository
public class PersonDAOImpl extends AbstractHibernateDAO implements PersonDAO {
	
	public void delete(Person entity) {
		Session session = getCurrentSession();
		session.beginTransaction();
		session.delete(entity);
		session.getTransaction().commit();
	}

	@SuppressWarnings("unchecked")
	public List<Person> findAll() {
		List<Person> person = (List<Person>) getCurrentSession().createCriteria(Person.class).list();
		return person;
	}

	public void deleteAll() {
		List<Person> entityList = findAll();
		for (Person entity : entityList) {
			delete(entity);
		}
	}

	public Person findByUid(String uid) {
		Person person = (Person) getCurrentSession().createCriteria(Person.class)
				.add(Restrictions.eq("uid", uid))
				.list().get(0);
		return person;
	}

	public void persist(Person person) {
		Session session = getCurrentSession();
		session.beginTransaction();
		getCurrentSession().save(person);
		session.getTransaction().commit();
	}

	public Person findByNames(String names) {
		Person person = (Person) getCurrentSession().createCriteria(Person.class)
				.add(Restrictions.ilike("fullName", names.toLowerCase() + "%"))
				.list().get(0);
		return person;
	}

}