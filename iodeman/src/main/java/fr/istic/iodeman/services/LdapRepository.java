package fr.istic.iodeman.services;

import fr.istic.iodeman.models.Person;

public interface LdapRepository {

	public Person lookupPerson(String username);
	
	public Person searchByUID(String uid);
	
	public Person searchByMail(String mail);
	
}
