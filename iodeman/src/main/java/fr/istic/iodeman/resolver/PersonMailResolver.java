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
				personDAO.persist(person);
			}
		}
		
		return person;
	}
}
