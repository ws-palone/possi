package fr.istic.iodeman.dao;


import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import fr.istic.iodeman.model.Person;
 
public class PersonDAOImpl implements PersonDAO {
    private SessionFactory sessionFactory;
 
    public PersonDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
 
    @Override
    @Transactional
    public List<Person> list() {
        @SuppressWarnings("unchecked")
        List<Person> listUser = (List<Person>) sessionFactory.getCurrentSession()
                .createCriteria(Person.class)
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
 
        return listUser;
    }
 
}