package fr.istic.iodeman.dto;

import fr.istic.iodeman.models.Planning;

public class RevisionsDTO {
    private Number revision;
    private Planning planning;

    public RevisionsDTO(Number revision, Planning planning) {
        this.revision = revision;
        this.planning = planning;
    }

    public Number getRevision() {
        return revision;
    }

    public void setRevision(Number revision) {
        this.revision = revision;
    }

    public Planning getPlanning() {
        return planning;
    }

    public void setPlanning(Planning planning) {
        this.planning = planning;
    }
}
