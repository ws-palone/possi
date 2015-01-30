package fr.istic.iodeman.dao;


import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import fr.istic.iodeman.model.Person;
import fr.istic.iodeman.utils.HibernateUtil;

@Repository
public class PersonDAOImpl extends AbstractHibernateDAO implements PersonDAO {

	public void persist(Person person) {
		Session session = getNewSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.save(person);
			session.getTransaction().commit();	
		} catch (Exception e){
			if (transaction!=null) transaction.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public Person findByUid(String uid) {
		Session session = getNewSession();
		Person p =  (Person) session.createCriteria(Person.class)
				.add(Restrictions.eq("uid", uid))
				.uniqueResult();
		
		session.close();
		return p;
	}

	public void delete(Person entity) {
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
	public List<Person> findAll() {
		Session session = getNewSession();
		List<Person> person = (List<Person>) session.createCriteria(Person.class).list();
		session.close();
		return person;
	}

	public void deleteAll() {
		List<Person> entityList = findAll();
		for (Person entity : entityList) {
			delete(entity);
		}
	}

	public Person findByNames(String names) {
		Session session =  HibernateUtil.getSessionFactory().openSession();

		Person person = (Person) session.createCriteria(Person.class)
				.add(Restrictions.ilike("fullName", names.toLowerCase() + "%"))
				.list().get(0);
		return person;
	}

	public Person findByEmail(String email) {
		Session session = getNewSession();
		Person p =  (Person) session.createCriteria(Person.class)
				.add(Restrictions.eq("email", email))
				.uniqueResult();
		
		session.close();
		return p;
	}

}


