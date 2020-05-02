package fr.istic.iodeman.dto;

import fr.istic.iodeman.models.OralDefense;

import java.util.Collection;

public class ExtractParticipantResponseDTO {

    private Collection<OralDefense> data;
    private Collection<ExtractParticipantErrorDTO>errors;

    public Collection<OralDefense> getData() {
        return data;
    }

    public void setData(Collection<OralDefense> data) {
        this.data = data;
    }

    public Collection<ExtractParticipantErrorDTO> getErrors() {
        return errors;
    }

    public void setErrors(Collection<ExtractParticipantErrorDTO> errors) {
        this.errors = errors;
    }
}
