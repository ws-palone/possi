package fr.istic.iodeman.services;

import com.google.common.collect.Lists;
import fr.istic.iodeman.dao.PlanningDAO;
import fr.istic.iodeman.dao.UnavailabilityDAO;
import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.Unavailability;
import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class UnavailabilityServiceImpl implements UnavailabilityService{

	private final UnavailabilityDAO unavailabilityDAO;

	private final PlanningDAO planningDAO;

	public UnavailabilityServiceImpl(UnavailabilityDAO unavailabilityDAO, PlanningDAO planningDAO) {
		this.unavailabilityDAO = unavailabilityDAO;
		this.planningDAO = planningDAO;
	}

	public List<Unavailability> findById(Integer idPlanning, String uid) {
		return unavailabilityDAO.findById(idPlanning, uid);
	}

	public void save(Integer idPlanning, Collection<Unavailability> unavailabilities) {
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

}
