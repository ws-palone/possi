package fr.istic.iodeman.dto;

import fr.istic.iodeman.models.TimeBox;
import fr.istic.iodeman.models.Unavailability;

import java.util.Collection;

public class UnavailabilityAgendaDTO {
    private Collection<TimeBox> timeBoxes;
    private Collection<Unavailability> unavailabilities;

    public Collection<TimeBox> getTimeBoxes() {
        return timeBoxes;
    }

    public void setTimeBoxes(Collection<TimeBox> timeBoxes) {
        this.timeBoxes = timeBoxes;
    }

    public Collection<Unavailability> getUnavailabilities() {
        return unavailabilities;
    }

    public void setUnavailabilities(Collection<Unavailability> unavailabilities) {
        this.unavailabilities = unavailabilities;
    }
}
