package fr.istic.iodeman.dto;

import fr.istic.iodeman.model.Participant;

import java.util.Collection;

public class ExtractParticipantResponseDTO {

    private Collection<Participant>data;
    private Collection<ExtractParticipantErrorDTO>errors;

    public Collection<Participant> getData() {
        return data;
    }

    public void setData(Collection<Participant> data) {
        this.data = data;
    }

    public Collection<ExtractParticipantErrorDTO> getErrors() {
        return errors;
    }

    public void setErrors(Collection<ExtractParticipantErrorDTO> errors) {
        this.errors = errors;
    }
}
