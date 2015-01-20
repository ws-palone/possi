package fr.istic.iodeman.dao;


import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.springframework.transaction.annotation.Transactional;

import fr.istic.iodeman.model.Person;
import fr.istic.iodeman.model.Role;

public class PersonDAOImpl implements PersonDAO {
	private Session currentSession;
	private Transaction currentTransaction;


	public PersonDAOImpl() {

	}
	public Session openCurrentSession() {
		currentSession = getSessionFactory().openSession();
		return currentSession;
	}

	public Session openCurrentSessionwithTransaction() {
		currentSession = getSessionFactory().openSession();
		currentTransaction = currentSession.beginTransaction();
		return currentSession;
	}

	public void closeCurrentSession() {
		currentSession.close();
	}

	public void closeCurrentSessionwithTransaction() {
		currentTransaction.commit();
		currentSession.close();
	}

	private static SessionFactory getSessionFactory() {
		Configuration configuration = new Configuration().configure();
		StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
		.applySettings(configuration.getProperties());
		SessionFactory sessionFactory = configuration.buildSessionFactory(builder.build());
		return sessionFactory;
	}

	public Session getCurrentSession() {
		return currentSession;
	}

	public void setCurrentSession(Session currentSession) {
		this.currentSession = currentSession;
	}

	public Transaction getCurrentTransaction() {
		return currentTransaction;
	}

	public void setCurrentTransaction(Transaction currentTransaction) {
		this.currentTransaction = currentTransaction;
	}

	public void persist(Person person) {
		getCurrentSession().save(person);
	}

	public void update(Person person) {
		getCurrentSession().update(person);
	}

	public Person findById(String id) {
		Person person = (Person) getCurrentSession().get(Person.class, id);
		return person; 
	}

	public void delete(Person entity) {
		getCurrentSession().delete(entity);
	}

	@SuppressWarnings("unchecked")
	public List<Person> findAll() {
		List<Person> person = (List<Person>) getCurrentSession().createQuery("from Person").list();
		return person;
	}

	public void deleteAll() {
		List<Person> entityList = findAll();
		for (Person entity : entityList) {
			delete(entity);
		}
	}
	/*@Override
    @Transactional
    public List<Person> list() {
        @SuppressWarnings("unchecked")
        List<Person> listPerson = (List<Person>) sessionFactory.getCurrentSession()
                .createCriteria(Person.class)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();

        return listPerson;
    }*/
	@Override
	public List<Person> list() {

		return null;
	}
	@Override
	public Person findById(int Id) {

		Person person = (Person) getCurrentSession().get(Person.class, Id);

		return person;

	}

	public static void main (String[] args){

		PersonDAO persondao = new PersonDAOImpl();
		persondao.openCurrentSession();
		Transaction tx= persondao.openCurrentSession().beginTransaction();
		persondao.setCurrentTransaction(tx);
		Person person1 = new Person();
		person1.setFirstName("pauline");
		person1.setLastName("le verge");
		person1.setRole(Role.STUDENT);
		persondao.persist(person1);
		persondao.getCurrentTransaction().commit();

		persondao.openCurrentSession();
		tx= persondao.openCurrentSession().beginTransaction();
		persondao.setCurrentTransaction(tx);
		Person person2 = new Person();
		person2.setFirstName("islame");
		person2.setLastName("le verge");
		person2.setRole(Role.STUDENT);
		persondao.persist(person2);
		persondao.getCurrentTransaction().commit();

		persondao.openCurrentSession();
		tx= persondao.openCurrentSession().beginTransaction();
		persondao.setCurrentTransaction(tx);
		Person person3 = new Person();
		person3.setFirstName("david");
		person3.setLastName("le verge");
		person3.setRole(Role.STUDENT);
		persondao.persist(person3);
		persondao.getCurrentTransaction().commit();

		persondao.findById(person1.getId());
		persondao.findById(person2.getId());
		persondao.findById(person3.getId());

		persondao.findAll();


	}

}