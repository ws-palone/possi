package fr.istic.iodeman.service;

import fr.istic.iodeman.model.Participant;

import java.io.File;
import java.util.Collection;

public interface ParticipantService {

    public Collection<Participant> saveParticipants (Collection<Participant> participants);

    Collection<Participant> importPartcipants(File file) throws Exception;
}
