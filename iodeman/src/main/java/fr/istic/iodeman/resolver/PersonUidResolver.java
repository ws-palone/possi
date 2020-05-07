package fr.istic.iodeman.resolver;

import fr.istic.iodeman.models.Person;
import fr.istic.iodeman.repositories.PersonRepository;
import fr.istic.iodeman.utils.LdapHelper;
import org.springframework.stereotype.Component;

@Component
public class PersonUidResolver implements PersonResolver {

	private final PersonRepository personRepository;


	public PersonUidResolver(PersonRepository personRepository) {
		this.personRepository = personRepository;
	}

	public Person resolve(String uid) {
		Person person = personRepository.findByUid(uid);

		if (person == null) {
			LdapHelper ldapHelper = new LdapHelper();
			person = ldapHelper.getPersonByUid(uid);
			if (person != null) {
				return personRepository.save(person);
			}
		}

		return person;
	}


}
