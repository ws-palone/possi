package fr.istic.iodeman.services;

import fr.istic.iodeman.models.OralDefense;
import fr.istic.iodeman.repositories.OralDefenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class OralDefenseServiceImpl implements OralDefenseService {

    private final OralDefenseRepository oralDefenseRepository;

    public OralDefenseServiceImpl(OralDefenseRepository oralDefenseRepository) {
        this.oralDefenseRepository = oralDefenseRepository;
    }

    @Override
    public Iterable<OralDefense> save(Collection<OralDefense> oralDefenses) {
        return oralDefenseRepository.saveAll(oralDefenses);
    }


    @Override
    public void delete(Collection<OralDefense> oralDefenses) {
        oralDefenseRepository.deleteAll(oralDefenses);
    }
}
