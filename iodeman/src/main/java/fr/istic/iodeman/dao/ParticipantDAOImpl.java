package fr.istic.iodeman.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import fr.istic.iodeman.model.Participant;

public class ParticipantDAOImpl implements ParticipantDAO{
	
	 private SessionFactory sessionFactory;
	 
	    public ParticipantDAOImpl(SessionFactory sessionFactory) {
	        this.sessionFactory = sessionFactory;
	    }
	 
	    @Override
	    @Transactional
	    public List<Participant> findAll() {
	        @SuppressWarnings("unchecked")
	        List<Participant> listParticipant = (List<Participant>) sessionFactory.getCurrentSession()
	                .createCriteria(Participant.class)
	                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	 
	        return listParticipant;
	    }

}
