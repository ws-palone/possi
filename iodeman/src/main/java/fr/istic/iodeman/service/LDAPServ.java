package fr.istic.iodeman.service;

import java.util.List;

import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;

import org.apache.tomcat.util.http.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.NamingException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Service;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Service
public class LDAPServ {
	/*
	@Autowired
	private LdapTemplate ldap;
	
	private class PersonAttributeMapper implements AttributesMapper{

		@Override
		public String mapFromAttributes(Attributes arg0)
				throws javax.naming.NamingException {
			// TODO Auto-generated method stub
			return "truc";
		}
		
	}
	
	public Object lookupPerson(String username) {
		AndFilter af = new AndFilter();
		af.and(new EqualsFilter("uid", username));
		SearchControls sc = new SearchControls();
        return ldap.search("", af.encode(), sc, new PersonAttributeMapper());
    }
	
	public List<String> getAllPersonNames() {

		return ldap.search(
				query().where("objectclass").is("person"),
				new AbstractContextMapper<String>() {

					@Override
					protected String doMapFromContext(DirContextOperations ctx) {

						return ctx.getStringAttribute("cn");
					}
				});
	}

*/
	
}
