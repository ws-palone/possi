package fr.istic.iodeman.controllers;

import fr.istic.iodeman.ldap.LdapData;
import fr.istic.iodeman.ldap.LdapDataImpl;
import fr.istic.iodeman.models.Person;
import fr.istic.iodeman.models.Planning;
import fr.istic.iodeman.repositories.PersonRepository;
import fr.istic.iodeman.resolver.PersonMailResolver;
import fr.istic.iodeman.resolver.PersonUidResolver;
import fr.istic.iodeman.services.PlanningService;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RequestMapping("/persons")
@RestController
public class PersonController {
	
	final
	PersonUidResolver resolverUID;
	
	final
	PersonMailResolver resolverMail;

	private final PlanningService planningService;

	private final PersonRepository personRepository;

	public PersonController(PersonUidResolver resolverUID, PersonMailResolver resolverMail, PersonRepository personRepository, PlanningService planningService) {
		this.resolverUID = resolverUID;
		this.resolverMail = resolverMail;
		this.personRepository = personRepository;
		this.planningService = planningService;
	}

	@GetMapping("/{uid}")
	public Person ldap(@PathVariable("uid") String uid) {
	    return resolverUID.resolve(uid);
	}

	@PostMapping
	public Iterable<Person> create(@RequestBody Collection<Person> persons) {
		return personRepository.saveAll(persons);
	}

	@GetMapping
	public Iterable<Person> getPerson(){
		return personRepository.findAll();
	}

	@GetMapping("/{uid}/plannings")
	public List<Planning> planningsByPerson(@PathVariable("uid") String uid){
//		Fixme: A revoir
		return planningService.findPersonBy(uid);
	}
}
