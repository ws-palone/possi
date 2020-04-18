package fr.istic.iodeman.services;

import fr.istic.iodeman.dto.ExtractParticipantResponseDTO;
import fr.istic.iodeman.models.Participant;
import fr.istic.iodeman.repositories.ParticipantRepository;
import fr.istic.iodeman.resolver.PersonMailResolver;
import fr.istic.iodeman.strategy.ParticipantsCSVImport;
import fr.istic.iodeman.strategy.ParticipantsImport;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collection;

@Service
public class ParticipantServiceImpl implements ParticipantService {

    private final ParticipantRepository participantRepository;

    private final PersonMailResolver personResolver;

    public ParticipantServiceImpl(ParticipantRepository participantRepository, PersonMailResolver personResolver) {
        this.participantRepository = participantRepository;
        this.personResolver = personResolver;
    }

    @Override
    public Iterable<Participant> saveParticipants(Collection<Participant> participants) {
        return participantRepository.saveAll(participants);
    }
    
    @Override
    public ExtractParticipantResponseDTO extractParticipantFromCSV(File file) throws Exception {
        ParticipantsImport participantsImport = new ParticipantsCSVImport();
        participantsImport.configure(personResolver);
        ExtractParticipantResponseDTO extractParticipantResponseDTO = new ExtractParticipantResponseDTO();
        extractParticipantResponseDTO.setData(participantsImport.execute(file));
        extractParticipantResponseDTO.setErrors(participantsImport.getErrorsImport());
        return extractParticipantResponseDTO;
    }

}
