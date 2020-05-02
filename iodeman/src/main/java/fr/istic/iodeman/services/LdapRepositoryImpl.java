package fr.istic.iodeman.services;

import java.util.List;

import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Service;

import fr.istic.iodeman.models.Person;
import fr.istic.iodeman.models.Role;

@Service
public class LdapRepositoryImpl implements LdapRepository {
	
	@Autowired
	private LdapTemplate ldap;
	
	private static class PersonAttributeMapper implements AttributesMapper{
		public Person mapFromAttributes(Attributes attrtibutes)
				throws javax.naming.NamingException {
			
			System.err.println(attrtibutes);
			System.err.println(attrtibutes.get("uid").get().toString());
			System.err.println(attrtibutes.get("givenName").get().toString());
			System.err.println(attrtibutes.get("sn").get().toString());
			System.err.println(attrtibutes.get("mail").get().toString());
			System.err.println(attrtibutes.get("mail").get().toString());
			 
			
			Person person = new Person();
			person.setUid(attrtibutes.get("uid").get().toString());
			person.setFirstName(attrtibutes.get("givenName").get().toString());
			person.setLastName(attrtibutes.get("sn").get().toString());
			person.setEmail(attrtibutes.get("mail").get().toString());
			
			if(attrtibutes.get("ur1TypeEntree").get().toString().equals("etu")) {
				person.setRole(Role.STUDENT);
			} else {
				person.setRole(Role.TEACHER);
			}
			
			return person;
		}
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
