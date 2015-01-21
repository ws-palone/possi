package fr.istic.iodeman.dao;

import org.hibernate.Session;

import fr.istic.iodeman.utils.HibernateUtil;

public abstract class AbstractHibernateDAO {

	Session session;
	
	protected Session getCurrentSession() {
		if (session == null) {
			session =  HibernateUtil.getSessionFactory().openSession();
		}
		return session;
	}
	
}
