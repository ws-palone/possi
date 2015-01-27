package fr.istic.iodeman.resolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.istic.iodeman.dao.PersonDAO;
import fr.istic.iodeman.model.Person;

@Component
public class PersonResolverImpl implements PersonResolver {

	@Autowired
	private PersonDAO personDAO;
	
	public Person resolve(String uid) {
		
		Person person = personDAO.findByUid(uid);
		
		if (person == null) {
			// check in LdapRepository
			
		}
		
		return person;
	}

	
}
