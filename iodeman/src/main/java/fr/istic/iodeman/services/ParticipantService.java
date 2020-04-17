package fr.istic.iodeman.services;

import fr.istic.iodeman.dto.ExtractParticipantResponseDTO;
import fr.istic.iodeman.model.Participant;

import java.io.File;
import java.util.Collection;

public interface ParticipantService {

    Collection<Participant> saveParticipants (Collection<Participant> participants);

    ExtractParticipantResponseDTO extractParticipantFromCSV(File file) throws Exception;

}
