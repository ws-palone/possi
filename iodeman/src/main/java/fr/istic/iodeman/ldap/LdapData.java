package fr.istic.iodeman.ldap;

import java.util.Map;

public interface LdapData {
	public Map<String,String> getPersonByUid(String uid);
	public Map<String,String> getPersonByEmail(String email);
}
