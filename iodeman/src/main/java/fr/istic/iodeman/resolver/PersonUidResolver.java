package fr.istic.iodeman.resolver;

import fr.istic.iodeman.dao.PersonDAO;
import fr.istic.iodeman.dao.PersonDAOImpl;
import fr.istic.iodeman.model.Person;
import fr.istic.iodeman.service.LdapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PersonUidResolver implements PersonResolver {

	@Autowired
	private PersonDAO personDAO;
	
	@Autowired
	private LdapRepository ldapRepository;
	
	public Person resolve(String uid) {
		personDAO = new PersonDAOImpl();
		Person person = personDAO.findByUid(uid);
		
		if (person == null) {
			person = ldapRepository.searchByUID(uid);
			if (person != null) {
				personDAO.persist(person);
			}
		}
		
		return person;
	}

	
}
