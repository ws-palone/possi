package fr.istic.iodeman.service;

import java.util.List;

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
			person.setFirstName(attrtibutes.get("givenName").get().toString());
			person.setLastName(attrtibutes.get("ur1nomusuel").get().toString());
			person.setEmail(attrtibutes.get("mail").get().toString());
			
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
		List<Person> results = search(af);
		return results.size() > 0 ? results.get(0) : null;
    }
	
	public Person searchByUID(String uid) {
		System.err.println("UID : On cherche pour " + uid);
		AndFilter af = new AndFilter();
		af.and(new EqualsFilter("uid", uid));
		List<Person> results = search(af);
		return results.size() > 0 ? results.get(0) : null;
	}
	
	public Person searchByMail(String mail) {
		AndFilter af = new AndFilter();
		af.and(new EqualsFilter("objectclass", "person"));
		af.and(new EqualsFilter("mail", mail));
		List<Person> results = search(af);
		return results.size() > 0 ? results.get(0) : null;
    }
	
	@SuppressWarnings("unchecked")
	private List<Person> search(AndFilter af) {
		SearchControls sc = new SearchControls();
		System.err.println("Requête LDAP : af " + af.encode());
		System.err.println("Requête LDAP : sc " + sc);
		
        return (List<Person>) ldap.search("", af.encode(), sc, new PersonAttributeMapper());
	}
	
}
