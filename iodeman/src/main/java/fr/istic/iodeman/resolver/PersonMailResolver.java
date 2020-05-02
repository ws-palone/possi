package fr.istic.iodeman.resolver;

import fr.istic.iodeman.models.Person;
import fr.istic.iodeman.repositories.PersonRepository;
import fr.istic.iodeman.utils.LdapHelper;
import org.springframework.stereotype.Component;

@Component
public class PersonMailResolver implements PersonResolver {

	private final PersonRepository personRepository;

	private final LdapHelper ldapHelper;

	public PersonMailResolver(PersonRepository personRepository, LdapHelper ldapHelper) {
		this.personRepository = personRepository;
		this.ldapHelper = ldapHelper;
	}

	public Person resolve(String mail) {

		System.err.println("Mail : On cherche pour " + mail);

		Person person = personRepository.findByEmail(mail);

		System.err.println("On cherche via personDAO " + person);

		if (person == null) {
			person = ldapHelper.getPersonByEmail(mail);
			if (person != null) {
				System.err.println("On a trouvé, donc on persiste");
				return personRepository.save(person);
			}
		}

		return person;
	}
}
