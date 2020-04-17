package fr.istic.iodeman.services;

import fr.istic.iodeman.dao.ParticipantDAO;
import fr.istic.iodeman.dto.ExtractParticipantResponseDTO;
import fr.istic.iodeman.model.Participant;
import fr.istic.iodeman.resolver.PersonMailResolver;
import fr.istic.iodeman.strategy.ParticipantsCSVImport;
import fr.istic.iodeman.strategy.ParticipantsImport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collection;

@Service
public class ParticipantServiceImpl implements ParticipantService {

    @Autowired
    private ParticipantDAO participantDAO;

    @Autowired
    private PersonMailResolver personResolver;

    @Override
    public Collection<Participant> saveParticipants(Collection<Participant> participants) {
        for(Participant p : participants){
            participantDAO.persist(p);
        }
        return participants;
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
