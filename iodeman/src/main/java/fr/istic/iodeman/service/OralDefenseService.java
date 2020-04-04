package fr.istic.iodeman.service;

import fr.istic.iodeman.model.OralDefense;

import java.util.Collection;

public interface OralDefenseService {
    Collection<OralDefense> save(Collection<OralDefense> oralDefenses);

    Collection<OralDefense> update(Collection<OralDefense> oralDefenses);

   void delete(Collection<OralDefense> oralDefenses);
}
