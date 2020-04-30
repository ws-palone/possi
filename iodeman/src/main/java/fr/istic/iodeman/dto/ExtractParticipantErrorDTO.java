package fr.istic.iodeman.dto;

public class ExtractParticipantErrorDTO {

    private String typeError;

    public ExtractParticipantErrorDTO(String p){
        this.typeError = p;
    }

    public String getTypeError() {
        return typeError;
    }

}
