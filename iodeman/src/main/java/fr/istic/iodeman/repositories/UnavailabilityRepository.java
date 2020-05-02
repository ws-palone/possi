package fr.istic.iodeman.repositories;

import fr.istic.iodeman.models.Person;
import fr.istic.iodeman.models.Planning;
import fr.istic.iodeman.models.Unavailability;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UnavailabilityRepository extends CrudRepository<Unavailability, Long> {
    void deleteByPlanning(Planning planning);
    List<Unavailability> findByPlanning(Planning planning);
    List<Unavailability> findByPlanningAndPerson(Planning planning, Person person);
}
