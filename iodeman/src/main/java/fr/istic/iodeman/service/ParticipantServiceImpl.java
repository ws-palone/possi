package fr.istic.iodeman.service;

import fr.istic.iodeman.dao.ParticipantDAO;
import fr.istic.iodeman.error.ErrorImport;
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
    public Collection<Participant> importPartcipants(File file) throws Exception {
        ParticipantsImport participantsImport = new ParticipantsCSVImport();
        participantsImport.configure(personResolver);
        return participantsImport.execute(file);
    }

    @Override
    public Collection<ErrorImport> checkError(File file) throws Exception {
        ParticipantsImport participantsImport = new ParticipantsCSVImport();
        participantsImport.configure(personResolver);
        participantsImport.execute(file);
        return participantsImport.getErrorsImport();
    }
}
