package fr.istic.iodeman.config;

import com.unboundid.ldap.sdk.LDAPException;
import fr.istic.iodeman.utils.LdapHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.GeneralSecurityException;

@Configuration
public class LdapConfig {


    @Value("${ldap.url}")
    private String LDAP_URL;

    @Value("${ldap.base}")
    private String LDAP_BASE;

    @Bean
    public LdapHelper ldapHelper() throws GeneralSecurityException, LDAPException {
        return new LdapHelper(LDAP_URL, LDAP_BASE);
    }
}
