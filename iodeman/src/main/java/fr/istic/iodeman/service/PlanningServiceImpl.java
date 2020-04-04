package fr.istic.iodeman.service;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import fr.istic.iodeman.builder.PlanningExportBuilder;
import fr.istic.iodeman.dao.*;
import fr.istic.iodeman.dto.ParticipantDTO;
import fr.istic.iodeman.dto.PersonDTO;
import fr.istic.iodeman.model.*;
import fr.istic.iodeman.resolver.PersonMailResolver;
import fr.istic.iodeman.strategy.PlanningSplitter;
import fr.istic.iodeman.strategy.PlanningSplitterImpl;
import fr.istic.possijar.Creneau;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.Validate;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
public class PlanningServiceImpl implements PlanningService {

	@Value("${PERSIST_PATH}")
	private String PERSIST_PATH;

	@Autowired
	private PlanningDAO planningDAO;

	@Autowired
	private PriorityDAO priorityDAO;

	@Autowired
	ParticipantService participantService;

	@Autowired
	OralDefenseService oralDefenseService;

	@Autowired
	private PersonMailResolver personResolver;

	//@Autowired
	//private PersonDAO personDAO;

	@Autowired
	private UnavailabilityDAO unavailabilityDAO;

	public PlanningServiceImpl(){
		planningDAO = new PlanningDAOImpl();
		unavailabilityDAO = new UnavailabilityDAOImpl();
		priorityDAO =  new PriorityDAOImpl();
		personResolver = new PersonMailResolver();
		//personDAO = new PersonDAOImpl();
	}


/*
	@Override
	public List<Planning> findAll(Planning planningByEtat) {
		return null;
	}
*/
	public Planning findById(Integer id) {
		return planningDAO.findById(id);
	}

	/*public List<Planning> findAll(Planning byEtat) {
		return planningDAO.findAll();
	}*/

	@Override
	public Planning save(Planning planning) {
		Validate.notNull(planning);
		Validate.isTrue(planning.getOralDefenseDuration() > 0);
		planning.getPeriod().validate();
		planning.getDayPeriod().validate();
		planning.getLunchBreak().validate();
		planning.setIs_ref(1);
		planning.setEtat(0);
		planning.setPriorities(Lists.newArrayList(
				new Priority("ENTREPRISE", 1),
				new Priority("ENSEIGNANT", 2),
        		new Priority("HORAIRES", 3)
		));

		planning.setParticipants(participantService.saveParticipants(planning.getParticipants()));

		planningDAO.persist(planning);

		return planning;
	}

	public Planning update(Planning planning) {

//		Validate.notNull(planning);
//
//		if (name != null && !name.equals("")) {
//			planning.setName(name);
//		}
//		if (csvFile != null && !csvFile.equals("")) {
//			planning.setCsv_file(csvFile);
//		}
//
//		if (period != null) {
//			period.validate();
//			planning.setPeriod(period);
//		}
//
//		if (oralDefenseDuration != null && oralDefenseDuration > 0) {
//			planning.setOralDefenseDuration(oralDefenseDuration);
//		}
//
//		if (dayPeriod != null) {
//			dayPeriod.validate();
//			planning.setDayPeriod(dayPeriod);
//		}
//
//		if (lunchBreak != null) {
//			lunchBreak.validate();
//			planning.setLunchBreak(lunchBreak);
//		}
//
//		planning.setOralDefenseInterlude(oralDefenseInterlude);
//		planning.setNbMaxOralDefensePerDay(nbMaxOralDefensePerDay);
//
//		if (rooms != null) {
//			planning.setRooms(rooms);
//		}


		planningDAO.update(planning);

		return planning;

	}

	public List<Planning> findAllByUid(String uid){
		return planningDAO.findAll(uid);
	}

	public Planning addParticipants(Planning planning, Collection<Participant> participants){
		planning.setParticipants(participants);
		planningDAO.update(planning);
		return planning;
	}

	public Collection<Participant> findParticipants(Planning planning) {

		return planningDAO.findParticipants(planning);
	}

	@Override
	public Collection<Priority> findPriorities(Planning planning) {
		return planningDAO.findPriorities(planning);
	}

	@Override
	public Collection<Priority> updatePriorities(Planning planning,
			Collection<Priority> priorities) {

		for(final Priority priority : priorities) {
			Collection<Priority> result = Collections2.filter(planning.getPriorities(), new Predicate<Priority>() {
				@Override
				public boolean apply(Priority p) {
					return p.getId().equals(priority.getId());
				}
			});
			if (result != null && result.size() > 0) {
				Priority p = Lists.newArrayList(result).get(0);
				p.setWeight(priority.getWeight());
				priorityDAO.update(priority);
			}
		}

		return planning.getPriorities();
	}

	@Override
	public Map<Integer, List<Creneau>> exportJSON(Planning plan) {
		int idPlanning = plan.getId();
		Map<Integer, List<Creneau>> planning = null;
		File f = new File(PERSIST_PATH + "/" + idPlanning + "/planning.ser");
		if(f.exists()) {
			try(
					InputStream f1 = new FileInputStream(PERSIST_PATH + "/" + idPlanning + "/planning.ser");
					InputStream b1 = new BufferedInputStream(f1);
					ObjectInput i1 = new ObjectInputStream (b1);
					){
				//deserialize the List
				planning = (Map<Integer, List<Creneau>>)i1.readObject();


			}
			catch(ClassNotFoundException ex){
				System.err.println(ex);
			}
			catch(IOException ex){
				System.err.println(ex);
			}
		}

		return planning;
	}

	@Override
	public Integer duplicate(Integer id) {

		Integer newId = planningDAO.duplicate(id);

		File dir = new File(PERSIST_PATH + "/" + id);
		File newDir = new File(PERSIST_PATH+ "/" + newId);

		try {
			FileUtils.copyDirectory(dir, newDir);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return newId;
	}

	@Override
	public Integer duplicateDraft(Integer id) {

		Integer newId = planningDAO.duplicateDraft(id);

		File dir = new File(PERSIST_PATH + "/" + id);
		File newDir = new File(PERSIST_PATH + "/" + newId);

		try {
			FileUtils.copyDirectory(dir, newDir);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return newId;
	}

	@Override
	public List<Planning> findAllDrafts(Integer id) {
		List<Planning> plannings = new ArrayList<Planning>();
		plannings = planningDAO.findDrafts(id);

		return plannings;
	}

	@Override
	public void switchReference(Integer idDraft) {
		planningDAO.switchReference(idDraft);
	}

	@Override
	public void updateUnvailibilities(Integer planning_id, JSONArray jsonarray) {
//		for (int i = 0; i < jsonarray.length(); i++) {
//			//Récuperation depuis le fichier le json
//			JSONObject jsonobject = jsonarray.getJSONObject(i);
//			String student = jsonobject.getJSONObject("student").getString("name");
//			Integer periode = jsonobject.getInt("periode");
//
//			//Recuperation de la personne
//			PersonDAO person = new PersonDAOImpl();
//			Person p = person.findByEmail(student);
//
//			//Recuperation du planning
//			Planning planning = this.findById(planning_id);
//
//			//BDD
//			PlanningSplitter splitter = new PlanningSplitterImpl();
//			List<TimeBox> timeboxes = splitter.execute(planning);
//			TimeBox tb = timeboxes.get(periode);
//			timeboxes.remove(tb);
//
//			UnavailabilityService unavailabilityService = new UnavailabilityServiceImpl();
//			unavailabilityService.deleteAll(p.getId(), planning_id);
//
//			for (TimeBox timebox: timeboxes) {
//				unavailabilityService.save(planning_id, p.getUid(), timebox);
//			}
//
//		}
	}

	@Override
	public File exportExcel(Planning planning) {

		// verify the planning is not null
		Validate.notNull(planning);

		// retrieving of the unavailabilities
		Collection<Unavailability> unavailabilities = unavailabilityDAO.findByPlanningId(planning.getId());

		PlanningExportBuilder builder = new PlanningExportBuilder(planning);
		builder.setParticipants(planningDAO.findParticipants(planning));
		builder.setUnavailabilities(unavailabilities);

		File file = null;
		try {
//			file = builder.split().build().toCSV();
			file = builder.split().build().toXls();
		} catch (Exception e) {
			System.out.println("Erreur de l'exportation lors de la fonction exportExcel: "+e.getMessage());
			e.printStackTrace();
		}

		//Validate.isTrue(file.exists());

		return file;
	}

	@Override
	public File exportExcelWithoutBuild(Planning planning) {

		// verify the planning is not null
		Validate.notNull(planning);

		PlanningExportBuilder builder = new PlanningExportBuilder(planning);
		builder.setParticipants(planningDAO.findParticipants(planning));

		File file = null;
		try {
			System.out.println("Export without build");
//			file = builder.split().build().toCSV();
			file = builder.split().toXls();
		} catch (Exception e) {
			System.out.println("Erreur de l'exportation lors de la fonction exportExcel: "+e.getMessage());
			e.printStackTrace();
		}

		//Validate.isTrue(file.exists());

		return file;
	}

	@Override
	public Planning generate(Integer planningId) {

		// retrieving the planning
		Planning planning = planningDAO.findById(planningId);
		Validate.notNull(planning);

		// check if the current user is the admin of this planning
//		session.acceptOnly(planning.getAdmin());


		// initialize builder
		PlanningExportBuilder builder = new PlanningExportBuilder(planning);
		builder.setParticipants(planningDAO.findParticipants(planning));
		builder.setUnavailabilities(unavailabilityDAO.findByPlanningId(planning.getId()));

		// build & return
		if (!planning.getOralDefenses().isEmpty())
			oralDefenseService.delete(planning.getOralDefenses());

	 	planning.setOralDefenses(oralDefenseService.save(builder.split().build().getOralDefenses(personResolver)));

	 	planningDAO.update(planning);

	 	return planning;
	}

	@Override
	public Planning updateByPersonUnavailabilities(int planningId, String personUid) {
		Planning planning = planningDAO.findById(planningId);
		Validate.notNull(planning);
		PlanningExportBuilder builder = new PlanningExportBuilder(planning);

		oralDefenseService.update(builder.split().updatePlanning(unavailabilityDAO.findById(planning.getId(), personUid)));

		return planning;
	}

	@Override
	public void validate(Planning planning) {

		Validate.notNull(planning);

		// initialize builder
		PlanningExportBuilder builder = new PlanningExportBuilder(planning);
		builder.setParticipants(planningDAO.findParticipants(planning));

		builder.split().validate(); // throw exception if not validated

	}

	@Override
	public Collection<ParticipantDTO> findParticipantsAndUnavailabilitiesNumber(Planning planning) {
		// retrieving of participants
		Collection<Participant> participants = planningDAO.findParticipants(planning);

		// parsing for getting uids or person
		Collection<String> uids = Lists.newArrayList();
		for(Participant p : participants){
			if (!uids.contains(p.getStudent().getUid())) {
				uids.add(p.getStudent().getUid());
			}
			if (!uids.contains(p.getFollowingTeacher().getUid())) {
				uids.add(p.getFollowingTeacher().getUid());
			}
		}

		// retrieved map<Person, Integer> from parsing result
		Map<String, Integer> map = (HashMap<String, Integer>)planningDAO.findParticipantsAndUnavailabilitiesNumber(planning, uids);

		// create collection of ParticipantDTO
		Collection<ParticipantDTO> participantsDTO = Lists.newArrayList();

		for(Participant p : participants){
			// DTO structure
			ParticipantDTO participantDTO = new ParticipantDTO();
			PersonDTO studentDTO = new PersonDTO();
			PersonDTO followingTeacherDTO = new PersonDTO();
			participantDTO.setStudent(studentDTO);
			participantDTO.setFollowingTeacher(followingTeacherDTO);
			participantDTO.setTutorFullName(p.getTutorFullName());
			participantDTO.setCompany(p.getCompany());
			participantsDTO.add(participantDTO);

			// student data
			studentDTO.setPerson(p.getStudent());
			studentDTO.setUnavailabilitiesNumber(
					getUnavailabilitiesNumber(
							p.getStudent().getUid(),
							map
					)
			);

			// followingTeacher data
			followingTeacherDTO.setPerson(p.getFollowingTeacher());
			followingTeacherDTO.setUnavailabilitiesNumber(
					getUnavailabilitiesNumber(
							p.getFollowingTeacher().getUid(),
							map
					)
			);
		}

		// if the result is not null
		if (participantsDTO != null) return participantsDTO;

		return Lists.newArrayList();
	}

	private Integer getUnavailabilitiesNumber(String uid, Map<String, Integer> map){
		Integer nb = 0;
		if (map.containsKey(uid)){
			nb = map.get(uid);
		}
		return nb;
	}

	@Override
	public void delete(Planning planning) {

		planningDAO.deleteDraft(planning.getId());
		planningDAO.delete(planning);

	}

	@Override
	public List<Planning>  findPlanningByEtat() {
		return planningDAO.findByEtat();
	}


}
