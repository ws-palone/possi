package fr.istic.iodeman.error;

import fr.istic.iodeman.model.Participant;

public class ErrorImport {

    private String participant;
    private String typeError;

    public ErrorImport (String p, String e){
        this.participant = p;
        this.typeError = e;
    }

    public String getParticipant() {
        return participant;
    }

    public String getTypeError() {
        return typeError;
    }
}
