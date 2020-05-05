package fr.istic.iodeman.controllers;

import fr.istic.iodeman.models.*;
import fr.istic.iodeman.models.revision.PlanningRevision;
import fr.istic.iodeman.services.EntityRevisionService;
import fr.istic.iodeman.services.OralDefenseService;
import fr.istic.iodeman.services.PlanningService;
import fr.istic.iodeman.services.UnavailabilityService;
import org.apache.commons.lang.Validate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequestMapping("/plannings")
@RestController
public class PlanningController {

	private final PlanningService planningService;

	private final UnavailabilityService unavailabilityService;

	private final EntityRevisionService entityRevisionService;

	private final OralDefenseService oralDefenseService;

	public PlanningController(PlanningService planningService, UnavailabilityService unavailabilityService, EntityRevisionService entityRevisionService, OralDefenseService oralDefenseService) {
		this.planningService = planningService;
		this.unavailabilityService = unavailabilityService;
		this.entityRevisionService = entityRevisionService;
		this.oralDefenseService = oralDefenseService;
	}

	@GetMapping
	public Iterable<Planning> getAll(){
		return planningService.findAll();
	}

	@GetMapping("/{id}")
	public Planning findById(@PathVariable("id") Long id) {
		Planning planning =  planningService.findById(id);
		return unavailabilityService.setUnavailabilityByOralDefenses(planning);
	}

	@GetMapping("/{id}/revisions")
	public Collection<PlanningRevision> findRevision(@PathVariable("id") Long id) {
//		Todo retourner les versions d'un planning
		return entityRevisionService.getRevisions(id);
	}

	@PutMapping("/{id}/defaultrevision")
	public Planning setDefaultPlanning(@PathVariable("id") Long id, @RequestBody Long revId) {
		Planning planning = planningService.findById(id);
		planning.setDefaultRevision(entityRevisionService.findRevision(revId));
		return planningService.update(planning);
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

	@PutMapping
	public Planning updatePlanning(@RequestBody Planning planning) {
		// fixme verifier si c'est un admin
		return planningService.updateWithRevision(planning);
	}

	@PutMapping("/{id}/oraldefenses")
	public Planning updatePlanningOralDefenses(@PathVariable Long id, @RequestBody Collection<OralDefense> oralDefenses) {
		// fixme verifier si c'est un admin
		Planning planning = planningService.findById(id);
		oralDefenseService.save(oralDefenses, planning);
		planning.setNewUnavailabilities(false);
		return planningService.updateWithRevision(planning);
	}

	@GetMapping("/{id}/generate")
	public Planning generate(@PathVariable("id") Long id) {
		// FIXME: si c'est le créateur du planning
		return unavailabilityService.setUnavailabilityByOralDefenses(planningService.generate(id));
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

	@GetMapping("/{id}/createrevision")
	public Planning createRevision(@PathVariable("id") Long id) {
		// FIXME: si c'est le créateur du planning
		Planning planning = planningService.findById(id);
		planning.setNewUnavailabilities(false);
		return planningService.updateWithRevision(planning);
	}
}

