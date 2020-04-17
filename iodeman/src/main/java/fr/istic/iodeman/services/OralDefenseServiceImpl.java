package fr.istic.iodeman.services;

import fr.istic.iodeman.dao.OralDefenseDAO;
import fr.istic.iodeman.model.OralDefense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class OralDefenseServiceImpl implements OralDefenseService {

    @Autowired
    private OralDefenseDAO oralDefenseDAO;

    @Override
    public Collection<OralDefense> save(Collection<OralDefense> oralDefenses) {
        for (OralDefense oralDefense : oralDefenses) {
            oralDefenseDAO.persist(oralDefense);
        }
        return oralDefenses;
    }

    @Override
    public Collection<OralDefense> update(Collection<OralDefense> oralDefenses) {
        for (OralDefense oralDefense : oralDefenses) {
            oralDefenseDAO.update(oralDefense);
        }
        return oralDefenses;
    }

    @Override
    public void delete(Collection<OralDefense> oralDefenses) {
        for (OralDefense oralDefense : oralDefenses)
            oralDefenseDAO.delete(oralDefense);
    }
}
