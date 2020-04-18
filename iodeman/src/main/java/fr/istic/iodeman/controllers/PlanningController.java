package fr.istic.iodeman.controllers;

import fr.istic.iodeman.dto.RevisionsDTO;
import fr.istic.iodeman.models.*;
import fr.istic.iodeman.services.PlanningService;
import fr.istic.iodeman.services.UnavailabilityService;
import org.apache.commons.lang.Validate;
import org.springframework.data.history.Revision;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
		return planningService.findById(id);
	}

	@GetMapping("/{id}/revisions")
	public Collection<RevisionsDTO> findRevision(@PathVariable("id") Long id) {
		List<Revision<Long, Planning>> revisions = planningService.findRevision(id).getContent();
		Collection<RevisionsDTO> revisionsDTOS = new ArrayList<>();
		for (Revision<Long, Planning> revision : revisions) {
			revisionsDTOS.add(new RevisionsDTO(revision.getRequiredRevisionNumber(), revision.getEntity()));
		}
		return revisionsDTOS;
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
