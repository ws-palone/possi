package fr.istic.iodeman.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import fr.istic.iodeman.model.Priority;

	public class PriorityDAOImpl implements PriorityDAO {
		
		private SessionFactory sessionFactory;
		 
	    public PriorityDAOImpl(SessionFactory sessionFactory) {
	        this.sessionFactory = sessionFactory;
	    }
	 
	    @Override
	    @Transactional
	    public List<Priority> list() {
	        @SuppressWarnings("unchecked")
	        List<Priority> listPriority = (List<Priority>) sessionFactory.getCurrentSession()
	                .createCriteria(Priority.class)
	                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	 
	        return listPriority;
	    }

}
