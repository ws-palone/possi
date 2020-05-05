package fr.istic.iodeman.services;

import fr.istic.iodeman.models.revision.PlanningRevision;

import java.util.List;

public interface EntityRevisionService {
    void createRevision(Long id);

    List<PlanningRevision> getRevisions(Long id);

    PlanningRevision findRevision(Long id);

    PlanningRevision getLastRevision(Long id);
}
