package fr.istic.iodeman.controller;

import fr.istic.iodeman.dto.UnavailabilityAgendaDTO;
import fr.istic.iodeman.dto.UnavailabilityToUpdateDTO;
import fr.istic.iodeman.model.TimeBox;
import fr.istic.iodeman.model.Unavailability;
import fr.istic.iodeman.service.PlanningService;
import fr.istic.iodeman.service.UnavailabilityService;
import fr.istic.iodeman.strategy.PlanningSplitter;
import fr.istic.iodeman.strategy.PlanningSplitterImpl;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RequestMapping("/unavailability")
@RestController
public class UnavailabilityController {

	@Autowired
	private UnavailabilityService unavailabilityService;

	@Autowired
	private PlanningService planningService;

	@RequestMapping("/{id}/")
	public List<Unavailability> findById(@PathVariable("id") Integer id, @RequestParam("person") String uidperson ){
		List<Unavailability> unavailabilities = new ArrayList<Unavailability>();
		unavailabilities = unavailabilityService.findById(id, uidperson);

		return unavailabilities;
	}

	@RequestMapping(value = "/update/{planningId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Collection<Unavailability> update(@RequestBody UnavailabilityToUpdateDTO unavailability, @PathVariable("planningId") Integer planningId) {

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

	//	@RequestMapping("/{id}/delete")
//	public Collection<Unavailability> makeAvailable(
//			@PathVariable("id") Integer id,
//			@RequestParam("person") String uidperson,
//			@RequestParam("periodStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date periodStart,
//			@RequestParam("periodEnd") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date periodEnd
//			){
//
//		TimeBox timeBox = new TimeBox(
//				new DateTime(periodStart).toDate(),
//				new DateTime(periodEnd).toDate()
//		);
//
//		return unavailabilityService.delete(id, uidperson, timeBox);
//	}
//
	@RequestMapping("/agenda/{planningId}/{personUid}")
	public UnavailabilityAgendaDTO exportAgenda(@PathVariable("planningId") Integer planningId, @PathVariable("personUid") String personUid){
		UnavailabilityAgendaDTO unavailabilityAgendaDTO = new UnavailabilityAgendaDTO();
		PlanningSplitter planningSplitter = new PlanningSplitterImpl();
		unavailabilityAgendaDTO.setTimeBoxes(planningSplitter.execute(planningService.findById(planningId)));
		unavailabilityAgendaDTO.setUnavailabilities(unavailabilityService.findById(planningId, personUid));

		return unavailabilityAgendaDTO;
	}

}
