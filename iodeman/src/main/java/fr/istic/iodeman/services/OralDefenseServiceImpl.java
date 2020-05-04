package fr.istic.iodeman.services;

import fr.istic.iodeman.builder.PlanningExportBuilder;
import fr.istic.iodeman.dto.ExtractParticipantResponseDTO;
import fr.istic.iodeman.models.OralDefense;
import fr.istic.iodeman.models.Planning;
import fr.istic.iodeman.repositories.ColorRepository;
import fr.istic.iodeman.repositories.OralDefenseRepository;
import fr.istic.iodeman.resolver.PersonMailResolver;
import fr.istic.iodeman.resolver.PersonResolver;
import fr.istic.iodeman.strategy.ParticipantsCSVImport;
import fr.istic.iodeman.strategy.ParticipantsImport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collection;

@Service
public class OralDefenseServiceImpl implements OralDefenseService {

    private final OralDefenseRepository oralDefenseRepository;
    private final PersonResolver personResolver;
    private final ColorRepository colorRepository;

    public OralDefenseServiceImpl(OralDefenseRepository oralDefenseRepository, PersonMailResolver personResolver, ColorRepository colorRepository) {
        this.oralDefenseRepository = oralDefenseRepository;
        this.personResolver = personResolver;
        this.colorRepository = colorRepository;
    }

    @Override
    public Iterable<OralDefense> save(Collection<OralDefense> oralDefenses) {
        return oralDefenseRepository.saveAll(oralDefenses);
    }

    @Override
    public Iterable<OralDefense> save(Collection<OralDefense> oralDefenses, Planning planning) {
        PlanningExportBuilder builder = new PlanningExportBuilder(colorRepository);
        builder.setPlanning(planning);
        builder.split().updatePlanning(oralDefenses);
        return save(oralDefenses);
    }


    @Override
    public void delete(Collection<OralDefense> oralDefenses) {
        oralDefenseRepository.deleteAll(oralDefenses);
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
