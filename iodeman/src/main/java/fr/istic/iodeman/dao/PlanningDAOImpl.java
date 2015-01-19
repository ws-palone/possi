package fr.istic.iodeman.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import fr.istic.iodeman.model.Planning;

public class PlanningDAOImpl implements PlanningDAO {
	
	private SessionFactory sessionFactory;
	 
    public PlanningDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
 
    @Override
    @Transactional
    public List<Planning> list() {
        @SuppressWarnings("unchecked")
        List<Planning> listPlanning = (List<Planning>) sessionFactory.getCurrentSession()
                .createCriteria(Planning.class)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
 
        return listPlanning;
    }

}
