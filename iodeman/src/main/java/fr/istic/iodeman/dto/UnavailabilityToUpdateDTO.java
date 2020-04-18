package fr.istic.iodeman.dto;

import fr.istic.iodeman.models.Unavailability;

import java.util.List;

public class UnavailabilityToUpdateDTO {
    private List<Unavailability> toRemove;
    private List<Unavailability> toAdd;

    public List<Unavailability> getToRemove() {
        return toRemove;
    }

    public void setToRemove(List<Unavailability> toRemove) {
        this.toRemove = toRemove;
    }

    public List<Unavailability> getToAdd() {
        return toAdd;
    }

    public void setToAdd(List<Unavailability> toAdd) {
        this.toAdd = toAdd;
    }
}
