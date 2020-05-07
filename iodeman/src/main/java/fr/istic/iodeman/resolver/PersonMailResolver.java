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

		System.err.println("Mail : On cherche pour " + mail);

		Person person = personRepository.findByEmail(mail);

		System.err.println("On cherche via personDAO " + person);

		if (person == null) {
			LdapHelper ldapHelper = new LdapHelper();
			person = ldapHelper.getPersonByEmail(mail);
			if (person != null) {
				return personRepository.save(person);
			}
		}

		return person;
	}
}
