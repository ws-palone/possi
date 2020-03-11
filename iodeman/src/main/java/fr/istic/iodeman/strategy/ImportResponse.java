package fr.istic.iodeman.strategy;

import fr.istic.iodeman.error.ErrorImport;
import fr.istic.iodeman.model.Participant;

import java.util.Collection;

public class ImportResponse {

    private Collection<Participant>data;
    private Collection<ErrorImport>errors;

    public Collection<Participant> getData() {
        return data;
    }

    public void setData(Collection<Participant> data) {
        this.data = data;
    }

    public Collection<ErrorImport> getErrors() {
        return errors;
    }

    public void setErrors(Collection<ErrorImport> errors) {
        this.errors = errors;
    }
}
