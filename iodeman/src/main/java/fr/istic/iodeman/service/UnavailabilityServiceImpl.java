package fr.istic.iodeman.service;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.istic.iodeman.dao.PlanningDAO;
import fr.istic.iodeman.dao.UnavailabilityDAO;
import fr.istic.iodeman.dto.AgendaDTO;
import fr.istic.iodeman.model.Person;
import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.TimeBox;
import fr.istic.iodeman.model.Unavailability;
import fr.istic.iodeman.resolver.PersonUidResolver;

@Service
public class UnavailabilityServiceImpl implements UnavailabilityService{

	@Autowired
	private UnavailabilityDAO unavailabilityDAO;
	
	@Autowired
	private PlanningDAO planningDAO;
	
	@Autowired
	private PersonUidResolver personUidResolver;
	
	public List<Unavailability> findById(Integer idPlanning, String uid) {
		return unavailabilityDAO.findById(idPlanning, uid);
	}

	public Unavailability create(Integer idPlanning, String uid, TimeBox period) {
		Validate.notNull(idPlanning);
		Validate.notEmpty(uid);
		Validate.notNull(period);
		period.validate();
		
		Person person = personUidResolver.resolve(uid);
		Validate.notNull(person);
		
		Planning planning = planningDAO.findById(idPlanning);
		Validate.notNull(planning);
		
		Unavailability unavailability = new Unavailability();
		unavailability.setPeriod(period);
		unavailability.setPerson(person);
		unavailability.setPlanning(planning);
		
		unavailabilityDAO.persist(unavailability);
		
		return unavailability;
	}

	@Override
	public Unavailability delete(Integer id) {
		Validate.notNull(id);
		
		Unavailability unavailability = unavailabilityDAO.findById(id);
		Validate.notNull(unavailability);
		
		unavailabilityDAO.delete(unavailability);
		
		return unavailability;
	}

	@Override
	public Collection<AgendaDTO> exportAgenda(Planning planning, Person person) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
