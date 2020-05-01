package fr.istic.iodeman.controllers;

import fr.istic.iodeman.dto.RevisionsDTO;
import fr.istic.iodeman.models.*;
import fr.istic.iodeman.models.revision.PlanningRevision;
import fr.istic.iodeman.services.EntityRevisionService;
import fr.istic.iodeman.services.PlanningService;
import fr.istic.iodeman.services.UnavailabilityService;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RequestMapping("/plannings")
@RestController
public class PlanningController {

	private final PlanningService planningService;

	private final UnavailabilityService unavailabilityService;

	private final EntityRevisionService entityRevisionService;

	public PlanningController(PlanningService planningService, UnavailabilityService unavailabilityService, EntityRevisionService entityRevisionService) {
		this.planningService = planningService;
		this.unavailabilityService = unavailabilityService;
		this.entityRevisionService = entityRevisionService;
	}

	@GetMapping
	public Iterable<Planning> getAll(){
		return planningService.findAll();
	}

	@GetMapping("/{id}")
	public Planning findById(@PathVariable("id") Long id) {
		Planning planning =  planningService.findById(id);
		if (planning != null) {
			Collection<OralDefense> oralDefenses = planning.getOralDefenses();
			for (OralDefense oralDefense : oralDefenses) {
				if(oralDefense.getNumber() != null) {
					oralDefense.setUnavailabilities(unavailabilityService.findById(planning.getId(), oralDefense.getFollowingTeacher().getUid())
							.stream().map(Unavailability::getPeriod).collect(Collectors.toList()));
					Person secondTeacher = oralDefense.getSecondTeacher();
					if (secondTeacher != null) {
						oralDefense.getUnavailabilities().addAll(unavailabilityService.findById(planning.getId(), secondTeacher.getUid())
								.stream().map(Unavailability::getPeriod).collect(Collectors.toList()));
					}
				}
			}
		}
		return planning;
	}

	@GetMapping("/{id}/revisions")
	public Collection<PlanningRevision> findRevision(@PathVariable("id") Long id) {
//		Todo retourner les versions d'un planning
		return entityRevisionService.getRevision(id);
	}

	@GetMapping("/find/{name}")
	public Planning findByName(@PathVariable("name") String name) {
		return planningService.findByName(name);
	}

	@PostMapping
	public Planning createPlanning(@RequestBody Planning planning) {
		// fixme verifier si c'est un admin
		return planningService.save(planning);
	}


	@GetMapping("/{id}/generate")
	public Planning generate(@PathVariable("id") Long id) {
		// FIXME: si c'est le créateur du planning
		return planningService.generate(id);
	}


	@DeleteMapping("/{id}")
	public void deletePlanning(@PathVariable("id") Long id){
		// FIXME: si c'est le créateur du planning
//		Todo: Supprimer les fichiers associés
		System.out.println(id);
		Planning planning = planningService.findById(id);
		System.out.println(planning);
		Validate.notNull(planning);

		// remove all depsendencies
		// unavailabilities
		unavailabilityService.deleteByPlanning(id);
		// planning
		planningService.delete(planning);
	}

}
