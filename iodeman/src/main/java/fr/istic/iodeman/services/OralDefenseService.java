package fr.istic.iodeman.services;


import fr.istic.iodeman.dto.ExtractParticipantResponseDTO;
import fr.istic.iodeman.models.OralDefense;

import java.io.File;
import java.util.Collection;

public interface OralDefenseService {
    Iterable<OralDefense> save(Collection<OralDefense> oralDefenses);

    void delete(Collection<OralDefense> oralDefenses);

    ExtractParticipantResponseDTO extractParticipantFromCSV(File outputFile)  throws Exception;
}
