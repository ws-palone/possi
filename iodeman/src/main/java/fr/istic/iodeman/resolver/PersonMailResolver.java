package fr.istic.iodeman.resolver;

import fr.istic.iodeman.models.Person;
import fr.istic.iodeman.repositories.PersonRepository;
import fr.istic.iodeman.services.LdapRepository;
import org.springframework.stereotype.Component;

@Component
public class PersonMailResolver implements PersonResolver {

	private final PersonRepository personRepository;
	
	private final LdapRepository ldapRepository;

	public PersonMailResolver(PersonRepository personRepository, LdapRepository ldapRepository) {
		this.personRepository = personRepository;
		this.ldapRepository = ldapRepository;
	}

	public Person resolve(String mail) {
		
		System.err.println("Mail : On cherche pour " + mail);
		
		Person person = personRepository.findByEmail(mail);
		
		System.err.println("On cherche via personDAO " + person);
		
		if (person == null) {
			try {
			person = ldapRepository.searchByMail(mail);
			} catch (Exception e) {
				e.printStackTrace();
				person = new Person();
				person.setEmail(mail);
				person.setFirstName("[LDAP Problem] " + mail.split("@")[0].split(".")[0]);
				//person.setLastName(mail.split("@")[0].split(".")[0]);
			}
			if (person != null) {
				System.err.println("On a trouvé, donc on persiste");
				return personRepository.save(person);
			}
		}
		
		return person;
	}
}
