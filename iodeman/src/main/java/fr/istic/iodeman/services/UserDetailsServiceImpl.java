package fr.istic.iodeman.services;

import fr.istic.iodeman.models.Person;
import fr.istic.iodeman.resolver.PersonMailResolver;
import fr.istic.iodeman.resolver.PersonUidResolver;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final PersonUidResolver personResolver;

    public UserDetailsServiceImpl(PersonUidResolver personResolver) {
        this.personResolver = personResolver;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Person person = personResolver.resolve(userName);
        return new User(person.getUid(), person.getEmail(), new ArrayList<>());
    }
}
