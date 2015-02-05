package fr.istic.iodeman.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.istic.iodeman.dto.AgendaDTO;
import fr.istic.iodeman.model.TimeBox;
import fr.istic.iodeman.model.Unavailability;
import fr.istic.iodeman.service.UnavailabilityService;


@RequestMapping("/unavailability") 
@RestController
public class UnavailabilityController {
	
	@Autowired
	private UnavailabilityService unavailabilityService;
	
	@RequestMapping("/{id}/")
	public List<Unavailability> findById(@PathVariable("id") Integer id, @RequestParam("person") String uidperson ){
		List<Unavailability> unavailabilities = new ArrayList<Unavailability>();
		unavailabilities = unavailabilityService.findById(id, uidperson);

		return unavailabilities;
	}
	
	// id => Planning id
	@RequestMapping("/{id}/create/")
	public Unavailability createUnavailability(
			@PathVariable("id") Integer id, 
			@RequestParam("person") String uidperson,
			@RequestParam("periodStart") String periodStart,
			@RequestParam("periodEnd") String periodEnd
			){
		
		TimeBox period = new TimeBox(
				new DateTime(periodStart).toDate(),
				new DateTime(periodEnd).toDate());
		
		Unavailability unavailability = unavailabilityService.create(id, uidperson, period);
		
		return unavailability;
	}
	
	// id => Unavailability id
	@RequestMapping("/{id}/delete/")
	public Unavailability deleteUnavailability(@PathVariable("id") Integer id){
		
		Unavailability unavailability = unavailabilityService.delete(id);
		
		return unavailability;
	}
	
	@RequestMapping("/agenda/{planningId}/{personId}")
	public Collection<AgendaDTO> exportAgenda(@PathVariable("planningId") Integer planningId, @PathVariable("personId") Integer personId){
		return null;
	}

}
