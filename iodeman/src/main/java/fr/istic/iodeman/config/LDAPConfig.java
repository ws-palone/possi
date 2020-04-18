package fr.istic.iodeman.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

@Configuration
public class LDAPConfig {
	
	@Autowired
    Environment env;
	
	@Value("${ldap.url}")
	private String LDAP_URL;

	@Value("${ldap.base}")
	private String LDAP_BASE;
	
    @Bean
    public LdapContextSource contextSource () {
        LdapContextSource contextSource= new LdapContextSource();
        contextSource.setUrl(LDAP_URL);
        contextSource.setBase(LDAP_BASE);
        contextSource.setAnonymousReadOnly(true);
        contextSource.afterPropertiesSet();
 
        return contextSource;
    }

    @Bean
    public LdapTemplate ldapTemplate() {
    	LdapTemplate ldap = new LdapTemplate(contextSource());
    	try {
			ldap.afterPropertiesSet();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return ldap;
    }


}
