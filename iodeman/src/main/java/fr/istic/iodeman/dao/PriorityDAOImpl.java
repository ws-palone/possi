package fr.istic.iodeman.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.springframework.transaction.annotation.Transactional;

import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.Priority;

	public class PriorityDAOImpl implements PriorityDAO {
		
		private Session currentSession;
		private Transaction currentTransaction;


		public PriorityDAOImpl() {

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

		public void persist(Priority prio) {
			getCurrentSession().save(prio);
		}

		public void update(Priority prio) {
			getCurrentSession().update(prio);
		}

		public Priority findById(String id) {
			Priority prio = (Priority) getCurrentSession().get(Priority.class, id);
			return prio; 
		}

		public void delete(Priority entity) {
			getCurrentSession().delete(entity);
		}

		@SuppressWarnings("unchecked")
		public List<Priority> findAll() {
			List<Priority> prio = (List<Priority>) getCurrentSession().createQuery("from priority").list();
			return prio;
		}

		public void deleteAll() {
			List<Priority> entityList = findAll();
			for (Priority entity : entityList) {
				delete(entity);
			}
		}

		@Override
		public Priority findById(int Id) {

			Priority prio = (Priority) getCurrentSession().get(Priority.class, Id);

			return prio;

	    }
}
