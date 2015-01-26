package fr.istic.iodeman.service;

import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Service;

import fr.istic.iodeman.model.Person;

@Service
public class LdapRepositoryImpl implements LdapRepository {
	
	@Autowired
	private LdapTemplate ldap;
	
	private class PersonAttributeMapper implements AttributesMapper{
		public Person mapFromAttributes(Attributes attrtibutes)
				throws javax.naming.NamingException {
			
			Person person = new Person();
			person.setUid(attrtibutes.get("uid").toString());
			person.setFirstName(attrtibutes.get("ur1prenom").toString());
			person.setLastName(attrtibutes.get("sn").toString());
			
			/*if (attrtibutes.get("edu").toString().equals("teacher")) {
				person.setRole(Role.PROF);
			}else{
				person.setRole(Role.STUDENT);
			}*/
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
