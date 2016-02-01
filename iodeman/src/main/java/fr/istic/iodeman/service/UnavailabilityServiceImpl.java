package fr.istic.iodeman.service;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import fr.istic.iodeman.dao.PlanningDAO;
import fr.istic.iodeman.dao.UnavailabilityDAO;
import fr.istic.iodeman.dto.AgendaDTO;
import fr.istic.iodeman.model.Person;
import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.TimeBox;
import fr.istic.iodeman.model.Unavailability;
import fr.istic.iodeman.resolver.PersonUidResolver;
import fr.istic.iodeman.strategy.ExportAgenda;
import fr.istic.iodeman.strategy.ExportJsonAgenda;
import fr.istic.iodeman.strategy.PlanningSplitter;
import fr.istic.iodeman.strategy.PlanningSplitterImpl;
import fr.istic.iodeman.utils.AlgoPlanningUtils;

@Service
public class UnavailabilityServiceImpl implements UnavailabilityService{

	@Autowired
	private UnavailabilityDAO unavailabilityDAO;
	
	@Autowired
	private PlanningDAO planningDAO;
	
	@Autowired
	private PersonUidResolver personUidResolver;
	
	@Autowired
	private MailService mailService;
	
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

		//mailService.notifyNewUnavailability(unavailability);
		
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
	public Collection<AgendaDTO> exportAgenda(Integer planningId, String personId) {
		// retrieving of all unavailabilities of the given person for the given planning
		List<Unavailability> unavailabilities = this.findById(planningId, personId);
		
		// retrieving of the planning 
		Planning planning = planningDAO.findById(planningId);
		
		// generation of all timeboxes for the given planning
		PlanningSplitter planningSplitter = new PlanningSplitterImpl();
		Collection<TimeBox> timeboxes = planningSplitter.execute(planning);
		
		// 
		ExportAgenda exportAgenda = new ExportJsonAgenda();
		Collection<AgendaDTO> agendaDtos = exportAgenda.execute(timeboxes, unavailabilities);
		
		return agendaDtos;
	}

	@Override
	public Collection<Unavailability> delete(Integer planningId, String uid, TimeBox period) {
		
		// retrieving of the planning 
		Planning planning = planningDAO.findById(planningId);
		Validate.notNull(planning);
		
		// retrieving of all unavailabilities of the given person for the given planning
		List<Unavailability> unavailabilities = this.findById(planningId, uid);
		
		Collection<Unavailability> deleted = Lists.newArrayList();
		
		// delete all the unavailabilities that make the period unavailable
		for(Unavailability ua : unavailabilities) {
			if (!AlgoPlanningUtils.isAvailable(ua, period)) {
				unavailabilityDAO.delete(ua);
				deleted.add(ua);
			}
		}
		
		for(Unavailability ua : deleted) {
			//mailService.notifyUnavailabityRemoved(ua);
		}
		
		return deleted;
		
	}

	@Override
	public void deleteByPlanning(Integer planningId) {
		unavailabilityDAO.deleteByPlanning(planningId);
	}
	
}
