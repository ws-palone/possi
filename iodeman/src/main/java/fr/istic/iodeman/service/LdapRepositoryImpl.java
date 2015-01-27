package fr.istic.iodeman.service;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Service;

import fr.istic.iodeman.model.Person;
import fr.istic.iodeman.model.Role;

@Service
public class LdapRepositoryImpl implements LdapRepository {
	
	@Autowired
	private LdapTemplate ldap;
	
	private class PersonAttributeMapper implements AttributesMapper{
		public Person mapFromAttributes(Attributes attrtibutes)
				throws javax.naming.NamingException {
			
			Person person = new Person();
			person.setUid(attrtibutes.get("uid").get().toString());
			person.setFirstName(attrtibutes.get("ur1prenom").get().toString());
			person.setLastName(attrtibutes.get("sn").get().toString());
			
			NamingEnumeration affiliations = attrtibutes.get("eduPersonAffiliation").getAll();
			while(affiliations.hasMore()) {
				if (affiliations.next().equals("teacher")) {
					person.setRole(Role.PROF);
				}
			}
			if (person.getRole() == null) {
				person.setRole(Role.STUDENT);
			}
			return person;
		}
	}
	
	public Person lookupPerson(String username) {
		AndFilter af = new AndFilter();
		af.and(new EqualsFilter("displayName", username));
		SearchControls sc = new SearchControls();
        return (Person) ldap.search("", af.encode(), sc, new PersonAttributeMapper()).get(0);
    }
	
	public Person searchByUID(String uid) {
		AndFilter af = new AndFilter();
		af.and(new EqualsFilter("uid", uid));
		SearchControls sc = new SearchControls();
        return (Person) ldap.search("", af.encode(), sc, new PersonAttributeMapper()).get(0);
	}
	
}
