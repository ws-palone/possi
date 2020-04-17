package fr.istic.iodeman.controllers;

import fr.istic.iodeman.dao.PersonDAO;
import fr.istic.iodeman.model.Person;
import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.Role;
import fr.istic.iodeman.resolver.PersonMailResolver;
import fr.istic.iodeman.resolver.PersonUidResolver;
import fr.istic.iodeman.services.PlanningService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RequestMapping("/persons")
@RestController
public class PersonController {
	
	final
	PersonUidResolver resolverUID;
	
	final
	PersonMailResolver resolverMail;

	private final PlanningService planningService;

	private final PersonDAO personDAO;

	public PersonController(PersonUidResolver resolverUID, PersonMailResolver resolverMail, PersonDAO personDAO, PlanningService planningService) {
		this.resolverUID = resolverUID;
		this.resolverMail = resolverMail;
		this.personDAO = personDAO;
		this.planningService = planningService;
	}

	@GetMapping("/{uid}")
	public Person ldap(@PathVariable("uid") String uid) {
	    return resolverUID.resolve(uid);
	}


	@GetMapping
	public Collection<Person> getPerson(){
		return personDAO.findAll();
	}

	@GetMapping("/{uid}/plannings")
	public List<Planning> planningsByPerson(@PathVariable("uid") String uid){
//		Fixme: A revoir
		return planningService.findAllByUid(uid);
	}
}
