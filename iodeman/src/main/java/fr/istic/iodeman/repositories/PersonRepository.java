package fr.istic.iodeman.repositories;

import fr.istic.iodeman.models.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, Long> {
    Person findDistinctByUid(String uid);
    Person findDistinctByEmail(String email);
}
