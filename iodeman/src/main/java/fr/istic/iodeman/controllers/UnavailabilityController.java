package fr.istic.iodeman.controllers;

import fr.istic.iodeman.dto.UnavailabilityAgendaDTO;
import fr.istic.iodeman.dto.UnavailabilityToUpdateDTO;
import fr.istic.iodeman.models.Unavailability;
import fr.istic.iodeman.services.PlanningService;
import fr.istic.iodeman.services.UnavailabilityService;
import fr.istic.iodeman.strategy.PlanningSplitter;
import fr.istic.iodeman.strategy.PlanningSplitterImpl;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RequestMapping("/unavailabilities")
@RestController
public class UnavailabilityController {

	private final UnavailabilityService unavailabilityService;


	private final PlanningService planningService;

	public UnavailabilityController(UnavailabilityService unavailabilityService, PlanningService planningService) {
		this.unavailabilityService = unavailabilityService;
		this.planningService = planningService;
	}

	@PostMapping(value = "/update/{planningId}")
	public Collection<Unavailability> update(@RequestBody UnavailabilityToUpdateDTO unavailability, @PathVariable("planningId") Long planningId) {

		unavailabilityService.save(planningId, unavailability.getToAdd());
		unavailabilityService.delete(planningId, unavailability.getToRemove());
		String uid;

		if (unavailability.getToAdd().isEmpty() && unavailability.getToRemove().isEmpty())
			return Collections.emptyList();
		else if (unavailability.getToAdd().isEmpty())
			uid = unavailability.getToRemove().get(0).getPerson().getUid();
		else
			uid = unavailability.getToAdd().get(0).getPerson().getUid();

		planningService.updateByPersonUnavailabilities(planningId, uid);
		return unavailabilityService.findById(planningId, uid);
	}

	@GetMapping("/agenda/{planningId}/{personUid}")
	public UnavailabilityAgendaDTO exportAgenda(@PathVariable("planningId") Long planningId, @PathVariable("personUid") String personUid){
		UnavailabilityAgendaDTO unavailabilityAgendaDTO = new UnavailabilityAgendaDTO();
		PlanningSplitter planningSplitter = new PlanningSplitterImpl();
		unavailabilityAgendaDTO.setTimeBoxes(planningSplitter.execute(planningService.findById(planningId)).getTimeBoxes());
		unavailabilityAgendaDTO.setUnavailabilities(unavailabilityService.findById(planningId, personUid));

		return unavailabilityAgendaDTO;
	}

}
