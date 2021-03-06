package fr.istic.iodeman.repositories.revision;

import fr.istic.iodeman.models.revision.PlanningRevision;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface PlanningRevisionRepository extends CrudRepository<PlanningRevision, Long> {
    List<PlanningRevision> findAllByPlanning_IdOrderByCreatedAt(Long id);
    List<PlanningRevision> findByPlanning_IdOrderByCreatedAtDesc(Long id);
}
