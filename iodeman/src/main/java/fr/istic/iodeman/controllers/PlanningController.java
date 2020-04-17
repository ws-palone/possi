package fr.istic.iodeman.controllers;

import com.google.common.collect.Lists;
import fr.istic.iodeman.dto.ParticipantDTO;
import fr.istic.iodeman.model.*;
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

	public PlanningController(PlanningService planningService, UnavailabilityService unavailabilityService) {
		this.planningService = planningService;
		this.unavailabilityService = unavailabilityService;
	}

	@GetMapping
	public List<Planning> ckeckAllPlanning(){
		return planningService.findAll();
	}

	@GetMapping("/{id}")
	public Planning findById(@PathVariable("id") Integer id) {
		return planningService.findById(id);
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
	
	@GetMapping("/{id}/participants")
	public Collection<Participant> getParticipants(@PathVariable("id") Integer id) {
		// FIXME: si c'est le créateur du planning
		Planning planning = planningService.findById(id);

		if (planning != null)
			return planningService.findParticipants(planning);

		return Lists.newArrayList();

	}

	@GetMapping("/{id}/participants/unavailabilities")
	public Collection<ParticipantDTO> getParticipantsAndUnavailabilitiesNumber(@PathVariable("id") Integer id) {
		// FIXME: si c'est le créateur du planning
		Planning planning = planningService.findById(id);

		if (planning != null)
			return planningService.findParticipantsAndUnavailabilitiesNumber(planning);

		return Lists.newArrayList();

	}

	@GetMapping("/{id}/priorities")
	public Collection<Priority> getPriorities(@PathVariable("id") Integer id) {
		// FIXME: si c'est le créateur du planning
		Planning planning = planningService.findById(id);

		if (planning != null) {
			return planningService.findPriorities(planning);
		}

		return Lists.newArrayList();

	}

	@GetMapping("/{id}/generate")
	public Planning generate(@PathVariable("id") Integer id) {
		// FIXME: si c'est le créateur du planning
		return planningService.generate(id);
	}

	@DeleteMapping("/{id}")
	public void deletePlanning(@PathVariable("id") Integer id){
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
