package fr.istic.iodeman.dao;

import org.hibernate.Session;

import fr.istic.iodeman.utils.HibernateUtil;

public abstract class AbstractHibernateDAO {
	/**
	 * Return a new session. 
	 * DO NOT FORGET to close it in the finally of a try/catch
	 * @return Session
	 */
	protected Session getNewSession() {
		return HibernateUtil.getSessionFactory().openSession();
	}
	
}
