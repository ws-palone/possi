package fr.istic.iodeman.services;

import fr.istic.iodeman.models.OralDefense;
import fr.istic.iodeman.models.Planning;
import fr.istic.iodeman.models.Priority;
import fr.istic.iodeman.models.Room;
import fr.istic.iodeman.models.revision.OralDefenseRevision;
import fr.istic.iodeman.models.revision.PlanningRevision;
import fr.istic.iodeman.models.revision.RevInfo;
import fr.istic.iodeman.models.revision.RoomRevision;
import fr.istic.iodeman.repositories.PriorityRepository;
import fr.istic.iodeman.repositories.revision.OralDefenseRevisionRepository;
import fr.istic.iodeman.repositories.revision.PlanningRevisionRepository;
import fr.istic.iodeman.repositories.revision.RevInfoRepository;
import fr.istic.iodeman.repositories.revision.RoomRevisionRepository;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class EntityRevisionServiceImpl implements EntityRevisionService {

    private final OralDefenseRevisionRepository oralDefenseRevisionRepository;

    private final PlanningRevisionRepository planningRevisionRepository;

    private final RevInfoRepository revInfoRepository;

    private final RoomRevisionRepository roomRevisionRepository;

    private final PriorityRepository priorityRepository;


    public EntityRevisionServiceImpl(OralDefenseRevisionRepository oralDefenseRevisionRepository, PlanningRevisionRepository planningRevisionRepository, RevInfoRepository revInfoRepository, RoomRevisionRepository roomRevisionRepository, PriorityRepository priorityRepository) {
        this.oralDefenseRevisionRepository = oralDefenseRevisionRepository;
        this.planningRevisionRepository = planningRevisionRepository;
        this.revInfoRepository = revInfoRepository;
        this.roomRevisionRepository = roomRevisionRepository;
        this.priorityRepository = priorityRepository;
    }

    @Override
    public void createRevision(Planning p) {

        Validate.notNull(p);
        PlanningRevision planningRevision = new PlanningRevision(p);
        RevInfo revInfo = new RevInfo();

        revInfo = revInfoRepository.save(revInfo);

        planningRevision.setRevInfo(revInfo);

        Collection<Room> rooms = p.getRooms();
        Collection<RoomRevision> roomRevisions = new ArrayList<>();

        for (Room room : rooms)
            roomRevisions.add(new RoomRevision(room));

        roomRevisionRepository.saveAll(roomRevisions);

        planningRevision.setRooms(roomRevisions);

        Collection<Priority> priorities = new ArrayList<>();
        priorityRepository.findAll().forEach(priorities::add);
        planningRevision.setPriorities(priorities);

        planningRevisionRepository.save(planningRevision);

        Collection<OralDefense> oralDefenses = p.getOralDefenses();
        Collection<OralDefenseRevision> oralDefenseRevisions = new ArrayList<>();

        for (OralDefense oralDefense : oralDefenses) {
            OralDefenseRevision oralDefenseRevision = new OralDefenseRevision(oralDefense);
            oralDefenseRevision.setPlanning(planningRevision);
            oralDefenseRevisions.add(oralDefenseRevision);
        }

        oralDefenseRevisionRepository.saveAll(oralDefenseRevisions);

    }

    @Override
    public List<PlanningRevision> getRevision(Long id) {
        return planningRevisionRepository.findAllByPlanning_Id(id);
    }
}
