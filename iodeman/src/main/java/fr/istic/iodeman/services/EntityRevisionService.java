package fr.istic.iodeman.services;

import fr.istic.iodeman.models.Planning;
import fr.istic.iodeman.models.revision.PlanningRevision;

import java.util.List;

public interface EntityRevisionService {
    void createRevision(Planning p);

    List<PlanningRevision> getRevision(Long id);
}
