package fr.istic.iodeman.utils;

import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.SSLSocketFactory;
import com.unboundid.ldap.sdk.Attribute;
import com.unboundid.ldap.sdk.Filter;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.LDAPSearchException;
import com.unboundid.ldap.sdk.SearchRequest;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldap.sdk.SearchScope;
import com.unboundid.util.ssl.SSLUtil;
import com.unboundid.util.ssl.TrustAllTrustManager;
import fr.istic.iodeman.models.Person;
import fr.istic.iodeman.models.Role;

public class LdapHelper {
	private String ldapUrl;

	private String ldapBase;

	private SSLSocketFactory sslSocketFactory ;
	private LDAPConnection connection;

	public LdapHelper(String ldapUrl, String ldapBase) throws GeneralSecurityException, LDAPException {
		try {
		this.ldapUrl = ldapUrl;
		this.ldapBase = ldapBase;
		SSLUtil sslUtil = new SSLUtil(new TrustAllTrustManager());
		sslSocketFactory = sslUtil.createSSLSocketFactory();
		connection = new LDAPConnection(sslSocketFactory);
		connection.connect(this.ldapUrl, 636);
		} catch (Exception e) {
			
		}
	}

	public Person getPersonByUid(String uid) {
		return getPersonAux(uid, false);
	}

	public Person getPersonByEmail(String email) {
		return getPersonAux(email, true);
	}

	private Person getPersonAux(String search, boolean mail) {
		Map<String,String> infosPerson = new HashMap<String,String>();
		Filter filter;
		if (mail)
			filter = Filter.createEqualityFilter("mail", search);
		else
			filter = Filter.createEqualityFilter("uid", search);

		SearchRequest searchRequest = new SearchRequest(ldapBase, SearchScope.SUB, filter, "*");
		SearchResult searchResult;

		Person person = null;

		try {
			person = new Person();
			searchResult = connection.search(searchRequest);
			System.err.println(searchResult.getEntryCount());

			for (SearchResultEntry entry : searchResult.getSearchEntries()) {
				for (Attribute a : entry.getAttributes()) {
					String name = a.getName();
					String value = a.getValue();
					if (name.equals("givenName"))
						person.setFirstName(value);
					else if (name.equals("mail"))
						person.setEmail(value);
					else if (name.equals("sn"))
						person.setLastName(value);
					else if (name.equals("uid"))
						person.setUid(value);
					else if (name.equals("employeeType")) {
						if (value.equals("personnel_titulaire_enseignantChercheur"))
							person.setRole(Role.TEACHER);
						else
							person.setRole(Role.STUDENT);
					}
				}

			}
		} catch(LDAPSearchException lse) {
			// The search failed for some reason.
			lse.printStackTrace();
		}
		return person.getEmail() == null ? null : person;
	}

}
