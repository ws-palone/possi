package fr.istic.iodeman.service;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import javax.naming.Context;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.springframework.stereotype.Service;

@Service
public class LDAPServiceImpl implements LADPService {

	@Override
	public List<String> getAllPersonName(){
		Hashtable env = new Hashtable();
	      env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
	      env.put(Context.PROVIDER_URL, "ldapglobal.univ-rennes1.fr");

	      DirContext ctx;
	      try {
	         ctx = new InitialDirContext(env);
	      } catch (NamingException e) {
	         throw new RuntimeException(e);
	      }
	      
	      List<String> list = new LinkedList<String>();
	      
	      NamingEnumeration results = null;
	      try {
	         SearchControls controls = new SearchControls();
	         controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
	         results = ctx.search("", "(objectclass=person)", controls);

	         while (results.hasMore()) {
	            SearchResult searchResult = (SearchResult) results.next();
	            Attributes attributes = searchResult.getAttributes();
	            Attribute attr = attributes.get("cn");
	            String cn = attr.get().toString();
	            list.add(cn);
	         }
	      } catch (NameNotFoundException e) {
	         // The base context was not found.
	         // Just clean up and exit.
	      } catch (NamingException e) {
	         throw new RuntimeException(e);
	      } finally {
	         if (results != null) {
	            try {
	               results.close();
	            } catch (Exception e) {
	               // Never mind this.
	            }
	         }
	         if (ctx != null) {
	            try {
	               ctx.close();
	            } catch (Exception e) {
	               // Never mind this.
	            }
	         }
	      }
	      return list;
	   }	

}
