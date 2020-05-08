package fr.istic.iodeman.resolver;

import fr.istic.iodeman.models.Person;
import fr.istic.iodeman.repositories.PersonRepository;
import fr.istic.iodeman.utils.LdapHelper;
import org.springframework.stereotype.Component;

@Component
public class PersonMailResolver implements PersonResolver {

	private final PersonRepository personRepository;

	public PersonMailResolver(PersonRepository personRepository) {
		this.personRepository = personRepository;
	}

	public Person resolve(String mail) {

		mail = mail.trim().toLowerCase();

		Person person = personRepository.findByEmailIgnoreCase(mail);

		if (person == null) {
			LdapHelper ldapHelper = new LdapHelper();
			person = ldapHelper.getPersonByEmail(mail);
			if (person != null) {
				person.setUid(person.getUid().trim().toLowerCase());
				person.setEmail(person.getEmail().trim().toLowerCase());
				return personRepository.save(person);
			}
		}

		return person;
	}
}
