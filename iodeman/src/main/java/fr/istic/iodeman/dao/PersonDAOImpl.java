package fr.istic.iodeman.dao;


import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Service;

import fr.istic.iodeman.model.Person;

@Service
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

	public Person findById(int id) {
		return (Person) getCurrentSession().get(Person.class, id);
	}

	@Override
	public void persist(Person person) {
		Session session = getCurrentSession();
		session.beginTransaction();
		getCurrentSession().save(person);
		session.getTransaction().commit();
	}

}