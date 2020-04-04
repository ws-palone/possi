package fr.istic.iodeman.service;

import com.google.common.collect.Lists;
import fr.istic.iodeman.dao.PlanningDAO;
import fr.istic.iodeman.dao.PlanningDAOImpl;
import fr.istic.iodeman.dao.UnavailabilityDAO;
import fr.istic.iodeman.dao.UnavailabilityDAOImpl;
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
import fr.istic.possijar.Creneau;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

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

	public UnavailabilityServiceImpl(){
		personUidResolver = new PersonUidResolver();
		planningDAO = new PlanningDAOImpl();
		unavailabilityDAO = new UnavailabilityDAOImpl();

	}

	public List<Unavailability> findById(Integer idPlanning, String uid) {
		return unavailabilityDAO.findById(idPlanning, uid);
	}

	public Collection<Unavailability> save(Integer idPlanning, Collection<Unavailability> unavailabilities) {
		//Validation
		Validate.notNull(idPlanning);
		if (!unavailabilities.isEmpty()) {
			Planning planning = planningDAO.findById(idPlanning);
			Validate.notNull(planning);

			if (planning.getIs_ref() == 0){
				Integer refId = planning.getRef_id();
				planning = planningDAO.findById(refId);
			}

			for (Unavailability unavailability : unavailabilities) {
				unavailability.setPlanning(planning);
				unavailabilityDAO.persist(unavailability);
			}

		}
		return unavailabilities;
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
	public Collection<Unavailability> delete(Integer idPlanning, Collection<Unavailability> unavailabilities) {
		Collection<Unavailability> deleted = Lists.newArrayList();

		//Validation
		Validate.notNull(idPlanning);
		if (!unavailabilities.isEmpty()) {
			Planning planning = planningDAO.findById(idPlanning);
			Validate.notNull(planning);

			// delete all the unavailabilities that make the period unavailable
			for(Unavailability ua : unavailabilities) {
				ua.setPlanning(planning);
				unavailabilityDAO.delete(ua);
				deleted.add(ua);
			}
		}

		return deleted;

	}

	@Override
	public void deleteByPlanning(Integer planningId) {
		unavailabilityDAO.deleteByPlanning(planningId);
	}

	@Override
	public void deleteAll(Integer id, Integer ref_id) {
		unavailabilityDAO.deleteAll(id, ref_id);
	}

	@Override
	public List<Date> getUnavailabilities(Integer planning_ref_Id, Creneau creneaux) {
		return unavailabilityDAO.getUnavailabilities(planning_ref_Id, creneaux);

	}


}
