package fr.istic.iodeman.repositories;

import fr.istic.iodeman.models.Planning;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.history.RevisionRepository;

import java.util.List;

public interface PlanningRepository extends CrudRepository<Planning, Long>, RevisionRepository<Planning, Long, Long> {
    Planning findByName(String name);
    List<Planning> findByAdmin_Uid(String uid);
}
