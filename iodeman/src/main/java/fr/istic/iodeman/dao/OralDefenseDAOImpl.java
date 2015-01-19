package fr.istic.iodeman.dao;


import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import fr.istic.iodeman.model.OralDefense;

 
public class OralDefenseDAOImpl implements OralDefenseDAO {
    private SessionFactory sessionFactory;
 
    public OralDefenseDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
 
    @Override
    @Transactional
    public List<OralDefense> list() {
        @SuppressWarnings("unchecked")
        List<OralDefense> listOralDefense = (List<OralDefense>) sessionFactory.getCurrentSession()
                .createCriteria(OralDefense.class)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
 
        return listOralDefense;
    }
 
}