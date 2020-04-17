package fr.istic.iodeman.dto;

public class ExtractParticipantErrorDTO {

    private String participant;

    public ExtractParticipantErrorDTO(String p){
        this.participant = p;
    }

    public String getParticipant() {
        return participant;
    }

}
