package fr.istic.iodeman.resolver;

import fr.istic.iodeman.models.Person;
import fr.istic.iodeman.repositories.PersonRepository;
import fr.istic.iodeman.services.LdapRepository;
import org.springframework.stereotype.Component;

@Component
public class PersonUidResolver implements PersonResolver {

	private final PersonRepository personRepository;
	
	private final LdapRepository ldapRepository;

	public PersonUidResolver(PersonRepository personRepository, LdapRepository ldapRepository) {
		this.personRepository = personRepository;
		this.ldapRepository = ldapRepository;
	}

	public Person resolve(String uid) {
		Person person = personRepository.findByUid(uid);
		
		if (person == null) {
			person = ldapRepository.searchByUID(uid);
			if (person != null) {
				return personRepository.save(person);
			}
		}
		
		return person;
	}

	
}
