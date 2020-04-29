package fr.istic.iodeman.controllers;

import fr.istic.iodeman.dto.RevisionsDTO;
import fr.istic.iodeman.models.*;
import fr.istic.iodeman.services.PlanningService;
import fr.istic.iodeman.services.UnavailabilityService;
import org.apache.commons.lang.Validate;
import org.springframework.data.history.Revision;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RequestMapping("/plannings")
@RestController
public class PlanningController {

	private final PlanningService planningService;

	private final UnavailabilityService unavailabilityService;

	public PlanningController(PlanningService planningService, UnavailabilityService unavailabilityService) {
		this.planningService = planningService;
		this.unavailabilityService = unavailabilityService;
	}

	@GetMapping
	public Iterable<Planning> getAll(){
		return planningService.findAll();
	}

	@GetMapping("/{id}")
	public Planning findById(@PathVariable("id") Long id) {
		Planning planning =  planningService.findById(id);
		Collection<OralDefense> oralDefenses = planning.getOralDefenses();
		for (OralDefense oralDefense : oralDefenses) {
			oralDefense.setUnavailabilities(unavailabilityService.findById(planning.getId(), oralDefense.getComposition().getFollowingTeacher().getUid())
					.stream().map(Unavailability::getPeriod).collect(Collectors.toList()));
			oralDefense.getUnavailabilities().addAll(unavailabilityService.findById(planning.getId(), oralDefense.getSecondTeacher().getUid())
					.stream().map(Unavailability::getPeriod).collect(Collectors.toList()));
		}
		return planning;
	}

	@GetMapping("/{id}/revisions")
	public Collection<RevisionsDTO> findRevision(@PathVariable("id") Long id) {
//		Todo retourner les versions d'un planning
		return null;
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
