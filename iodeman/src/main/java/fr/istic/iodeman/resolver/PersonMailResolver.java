package fr.istic.iodeman.resolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.istic.iodeman.dao.PersonDAO;
import fr.istic.iodeman.model.Person;
import fr.istic.iodeman.service.LdapRepository;

@Component
public class PersonMailResolver implements PersonResolver {

	@Autowired
	private PersonDAO personDAO;
	
	@Autowired
	private LdapRepository ldapRepository;
	
	public Person resolve(String mail) {
		
		System.err.println("Mail : On cherche pour " + mail);
		
		Person person = personDAO.findByEmail(mail);
		
		System.err.println("On cherche via personDAO " + person);
		
		if (person == null) {
			person = ldapRepository.searchByMail(mail);
			System.err.println("On cherche via ldapRepository " + person);
			if (person != null) {
				System.err.println("On a trouvé, donc on persiste");
				personDAO.persist(person);
			}
		}
		
		return person;
	}
}
