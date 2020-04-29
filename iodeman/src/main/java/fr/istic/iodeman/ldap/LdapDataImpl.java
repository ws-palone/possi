package fr.istic.iodeman.ldap;

import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.SSLSocketFactory;
import com.unboundid.ldap.sdk.Attribute;
import com.unboundid.ldap.sdk.Filter;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.LDAPSearchException;
import com.unboundid.ldap.sdk.ResultCode;
import com.unboundid.ldap.sdk.SearchRequest;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldap.sdk.SearchScope;
import com.unboundid.util.ssl.SSLUtil;
import com.unboundid.util.ssl.TrustAllTrustManager;

public class LdapDataImpl implements LdapData{
	private SSLUtil sslUtil;
	private SSLSocketFactory sslSocketFactory ;
	private LDAPConnection c;
	
	public LdapDataImpl() {
		sslUtil = new SSLUtil(new TrustAllTrustManager());
		try {
			sslSocketFactory = sslUtil.createSSLSocketFactory();
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c = new LDAPConnection(sslSocketFactory);
		try {
			c.connect("ldap.univ-rennes1.fr", 636);
		} catch (LDAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public LDAPConnection getConnection() {
		return this.c;
	}

	@Override
	public Map<String, String> getPersonByUid(String uid) {
		Map<String,String> infosPerson = new HashMap<String,String>();
		Filter filter = Filter.createEqualityFilter("uid", uid);
		SearchRequest searchRequest = new SearchRequest("ou=People,dc=univ-rennes1,dc=fr", SearchScope.SUB, filter, "*");
	    this.searchFactory(searchRequest, infosPerson);
		return infosPerson;
		
	}

	@Override
	public Map<String, String> getPersonByEmail(String email) {
		Map<String,String> infosPerson = new HashMap<String,String>();
		Filter filter = Filter.createEqualityFilter("mail", email);
		SearchRequest searchRequest = new SearchRequest("ou=People,dc=univ-rennes1,dc=fr", SearchScope.SUB, filter, "*");
		 this.searchFactory(searchRequest, infosPerson);
		return infosPerson;
		
	}
	
	private void searchFactory(SearchRequest searchRequest, Map<String,String> infosPerson) {
		SearchResult searchResult;
		try
		{
			searchResult = this.getConnection().search(searchRequest);
			for (SearchResultEntry entry : searchResult.getSearchEntries()) {
				for (Attribute a : entry.getAttributes()) {
					infosPerson.put(a.getName(), a.getValue());
				}
				
			}
		}catch(LDAPSearchException lse)
		{
			// The search failed for some reason.
			searchResult = lse.getSearchResult();
			ResultCode resultCode = lse.getResultCode();
			String errorMessageFromServer = lse.getDiagnosticMessage();
			lse.printStackTrace();

		}
	}

}
