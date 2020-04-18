package fr.istic.iodeman.services;


import fr.istic.iodeman.models.OralDefense;

import java.util.Collection;

public interface OralDefenseService {
    Iterable<OralDefense> save(Collection<OralDefense> oralDefenses);

    void delete(Collection<OralDefense> oralDefenses);
}
