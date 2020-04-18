package fr.istic.iodeman.repositories;

import fr.istic.iodeman.models.Participant;
import org.springframework.data.repository.CrudRepository;


public interface ParticipantRepository extends CrudRepository<Participant, Long> {
}
