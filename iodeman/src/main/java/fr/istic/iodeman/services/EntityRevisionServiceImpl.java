package fr.istic.iodeman.services;

import fr.istic.iodeman.models.OralDefense;
import fr.istic.iodeman.models.Planning;
import fr.istic.iodeman.models.Priority;
import fr.istic.iodeman.models.Room;
import fr.istic.iodeman.models.revision.OralDefenseRevision;
import fr.istic.iodeman.models.revision.PlanningRevision;
import fr.istic.iodeman.models.revision.RevInfo;
import fr.istic.iodeman.models.revision.RoomRevision;
import fr.istic.iodeman.repositories.PlanningRepository;
import fr.istic.iodeman.repositories.PriorityRepository;
import fr.istic.iodeman.repositories.revision.OralDefenseRevisionRepository;
import fr.istic.iodeman.repositories.revision.PlanningRevisionRepository;
import fr.istic.iodeman.repositories.revision.RevInfoRepository;
import fr.istic.iodeman.repositories.revision.RoomRevisionRepository;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Service
public class EntityRevisionServiceImpl implements EntityRevisionService {

    private final OralDefenseRevisionRepository oralDefenseRevisionRepository;

    private final PlanningRevisionRepository planningRevisionRepository;

    private final PlanningRepository planningRepository;

    private final RevInfoRepository revInfoRepository;

    private final RoomRevisionRepository roomRevisionRepository;

    private final PriorityRepository priorityRepository;


    public EntityRevisionServiceImpl(OralDefenseRevisionRepository oralDefenseRevisionRepository, PlanningRevisionRepository planningRevisionRepository, RevInfoRepository revInfoRepository, RoomRevisionRepository roomRevisionRepository, PriorityRepository priorityRepository, PlanningRepository planningRepository) {
        this.oralDefenseRevisionRepository = oralDefenseRevisionRepository;
        this.planningRevisionRepository = planningRevisionRepository;
        this.revInfoRepository = revInfoRepository;
        this.roomRevisionRepository = roomRevisionRepository;
        this.priorityRepository = priorityRepository;
        this.planningRepository = planningRepository;
    }

    @Override
    @Transactional
    public void createRevision(Long id) {

        Planning p = planningRepository.findById(id).get();
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
            System.out.println(oralDefense.getNumber());
            OralDefenseRevision oralDefenseRevision = new OralDefenseRevision(oralDefense);
            Iterator<RoomRevision> iterator = roomRevisions.iterator();
            boolean found = false;
            while (iterator.hasNext() && !found) {
                RoomRevision roomRevision = iterator.next();
                if (roomRevision.getName().equals(oralDefense.getRoom().getName())) {
                    oralDefenseRevision.setRoom(roomRevision);
                    found = true;
                }
            }
            oralDefenseRevision.setPlanning(planningRevision);
            oralDefenseRevisions.add(oralDefenseRevision);
        }

        oralDefenseRevisionRepository.saveAll(oralDefenseRevisions);

    }

    @Override
    public List<PlanningRevision> getRevisions(Long id) {
        return planningRevisionRepository.findAllByPlanning_IdOrderByCreatedAt(id);
    }

    @Override
    public PlanningRevision findRevision(Long id) {
        return planningRevisionRepository.findById(id).get();
    }

    @Override
    @Transactional
    public PlanningRevision getLastRevision(Long id) {
        return planningRevisionRepository.findByPlanning_IdOrderByCreatedAtDesc(id).get(0);
    }
}
