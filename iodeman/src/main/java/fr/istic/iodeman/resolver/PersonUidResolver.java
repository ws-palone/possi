package fr.istic.iodeman.resolver;

import fr.istic.iodeman.models.Person;
import fr.istic.iodeman.repositories.PersonRepository;
import fr.istic.iodeman.utils.LdapHelper;
import org.springframework.stereotype.Component;

@Component
public class PersonUidResolver implements PersonResolver {

	private final PersonRepository personRepository;

	private final LdapHelper ldapHelper;

	public PersonUidResolver(PersonRepository personRepository, LdapHelper ldapHelper) {
		this.personRepository = personRepository;
		this.ldapHelper = ldapHelper;
	}

	public Person resolve(String uid) {
		Person person = personRepository.findByUid(uid);

		if (person == null) {
			person = ldapHelper.getPersonByUid(uid);
			if (person != null && !person.getUid().isEmpty() && person.getRole() != null) {
				return personRepository.save(person);
			}
			person = null;
		}

		return person;
	}


}
