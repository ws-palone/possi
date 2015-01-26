package fr.istic.iodeman;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

@Configuration
public class LDAPConf {
	
	@Autowired
    Environment env;

    @Bean
    public LdapContextSource contextSource () {
        LdapContextSource contextSource= new LdapContextSource();
        contextSource.setUrl("ldap://ldapglobal.univ-rennes1.fr:389");
        contextSource.setBase("ou=people,dc=univ-rennes1,dc=fr");
     //   contextSource.setAuthenticationSource(authenticationSource);
        contextSource.setAnonymousReadOnly(true);
        contextSource.afterPropertiesSet();
  
        
        
        /*Map<String, Object> params = Maps.newHashMap();
        params.put("com.sun.jndi.connect.timeout", "120000");
        contextSource.setBaseEnvironmentProperties(params);*/
      //  contextSource.setAnonymousReadOnly(true);
      //  contextSource.setUserDn("uid=11008880");
        return contextSource;
    }

    @Bean
    public LdapTemplate ldapTemplate() {
    	LdapTemplate ldap = new LdapTemplate(contextSource());
    	try {
			ldap.afterPropertiesSet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return ldap;
    }


}
