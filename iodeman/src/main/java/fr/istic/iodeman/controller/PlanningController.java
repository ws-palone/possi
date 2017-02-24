package fr.istic.iodeman.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import fr.istic.iodeman.SessionComponent;
import fr.istic.iodeman.dto.ParticipantDTO;
import fr.istic.iodeman.model.*;
import fr.istic.iodeman.service.PlanningService;
import fr.istic.iodeman.service.RoomService;
import fr.istic.iodeman.service.UnavailabilityService;
import fr.istic.iodeman.strategy.PlanningSplitter;
import fr.istic.iodeman.strategy.PlanningSplitterImpl;
import fr.istic.possijar.Creneau;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.Validate;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.*;

@RequestMapping("/planning") 
@RestController
public class PlanningController {

	@Value("${PERSIST_PATH}")
	private String PERSIST_PATH;
	
	@Autowired
	private RoomService roomService;

	@Autowired
	private PlanningService planningService;
	
	@Autowired
	private UnavailabilityService unavailabilityService;

	@Autowired
	private SessionComponent session;
	
	@RequestMapping("/list")
	public List<Planning> listAll(){

		return planningService.findAllByUid(session.getUserUID());
	}

	@RequestMapping("/find/{id}")
	public Planning findById(@PathVariable("id") Integer id) {
		
		return planningService.findById(id);
	}
	
	@RequestMapping("/create")
	public Planning createPlanning(
			@RequestParam("name") String name,
			@RequestParam("periodStart") @DateTimeFormat(pattern="yyyy-MM-dd") Date periodStart,
			@RequestParam("periodEnd") @DateTimeFormat(pattern="yyyy-MM-dd") Date periodEnd,
			@RequestParam("oralDefenseDuration") Integer oralDefenseDuration,
			@RequestParam("oralDefenseInterlude") Integer oralDefenseInterlude, 
			@RequestParam("lunchBreakStart") @DateTimeFormat(pattern="HH:mm") Date lunchBreakStart,
			@RequestParam("lunchBreakEnd") @DateTimeFormat(pattern="HH:mm") Date lunchBreakEnd, 
			@RequestParam("dayPeriodStart") @DateTimeFormat(pattern="HH:mm") Date dayPeriodStart,
			@RequestParam("dayPeriodEnd") @DateTimeFormat(pattern="HH:mm") Date dayPeriodEnd,
			@RequestParam("nbMaxOralDefensePerDay") Integer nbMaxOralDefensePerDay,
			@RequestParam("rooms") List<String> rooms
			) {
		
		session.teacherOnly();
		
		//URL_TEST : http://iode-man-debian.istic.univ-rennes1.fr:8080/iodeman/planning/create?name=toto&periodStart=2015-01-01&periodEnd=2015-01-07&oralDefenseDuration=60&oralDefenseInterlude=15&lunchBreakStart=12:15&lunchBreakEnd=14:00&dayPeriodStart=08:00&dayPeriodEnd=18:15&nbMaxOralDefensePerDay=6&rooms=i51
		
		// check if the current user is a teacher
		Person user = session.getUser();
		//Validate.notNull(user);
		//Validate.isTrue(user.getRole() == Role.PROF);
		
		Collection<Room> roomsCollection = Collections2.transform(rooms, new Function<String, Room>() {
			@Override
			public Room apply(String roomName) {
				return roomService.findOrCreate(roomName);
			}
		});
		
		TimeBox period = new TimeBox(periodStart, periodEnd);
		
		TimeBox dayPeriod = new TimeBox(
				new DateTime(periodStart)
					.withHourOfDay(new DateTime(dayPeriodStart).getHourOfDay())
					.withMinuteOfHour(new DateTime(dayPeriodStart).getMinuteOfHour())
					.toDate(),
				new DateTime(periodStart)
					.withHourOfDay(new DateTime(dayPeriodEnd).getHourOfDay())
					.withMinuteOfHour(new DateTime(dayPeriodEnd).getMinuteOfHour())
					.toDate()
		);
		
		TimeBox lunch = new TimeBox(
				new DateTime(periodStart)
					.withHourOfDay(new DateTime(lunchBreakStart).getHourOfDay())
					.withMinuteOfHour(new DateTime(lunchBreakStart).getMinuteOfHour())
					.toDate(),
				new DateTime(periodStart)
					.withHourOfDay(new DateTime(lunchBreakEnd).getHourOfDay())
					.withMinuteOfHour(new DateTime(lunchBreakEnd).getMinuteOfHour())
					.toDate()
		);
		
		return planningService.create(user, name, period, oralDefenseDuration, oralDefenseInterlude, lunch, dayPeriod, nbMaxOralDefensePerDay, roomsCollection);
	}
	
	@RequestMapping("/update")
	public Planning updatePlanning(
			@RequestParam("planningID") Integer planningID,
			@RequestParam(value="name", required=false) String name,
			@RequestParam(value="periodStart", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date periodStart,
			@RequestParam(value="periodEnd", required=false) @DateTimeFormat(pattern="yyyy-MM-dd") Date periodEnd,
			@RequestParam(value="oralDefenseDuration", required=false) Integer oralDefenseDuration,
			@RequestParam(value="oralDefenseInterlude", required=false) Integer oralDefenseInterlude, 
			@RequestParam(value="lunchBreakStart", required=false) @DateTimeFormat(pattern="HH:mm") Date lunchBreakStart,
			@RequestParam(value="lunchBreakEnd", required=false) @DateTimeFormat(pattern="HH:mm") Date lunchBreakEnd, 
			@RequestParam(value="dayPeriodStart", required=false) @DateTimeFormat(pattern="HH:mm") Date dayPeriodStart,
			@RequestParam(value="dayPeriodEnd", required=false) @DateTimeFormat(pattern="HH:mm") Date dayPeriodEnd,
			@RequestParam(value="nbMaxOralDefensePerDay", required=false) Integer nbMaxOralDefensePerDay,
			@RequestParam(value="rooms", required=false) List<String> rooms,
			@RequestParam(value="csvFile", required=false) String csvFile
			) {
		
		Planning planning = planningService.findById(planningID);
		Validate.notNull(planning);
		
		// check if the current user is the admin of this planning
		session.acceptOnly(planning.getAdmin());
		
		Collection<Room> roomsCollection = null;
		
		if (rooms != null) {
			roomsCollection = Collections2.transform(rooms, new Function<String, Room>() {
				@Override
				public Room apply(String roomName) {
					return roomService.findOrCreate(roomName);
				}
			});
		}
		
		TimeBox period = (periodStart != null && periodEnd != null) ? new TimeBox(periodStart, periodEnd) : null;
		TimeBox lunch = (lunchBreakStart != null && lunchBreakEnd != null) ? new TimeBox(lunchBreakStart, lunchBreakEnd) : null;

		TimeBox dayPeriod = (dayPeriodStart != null && dayPeriodEnd != null) ? new TimeBox(
				new DateTime(planning.getDayPeriod().getFrom())
					.withHourOfDay(new DateTime(dayPeriodStart).getHourOfDay())
					.withMinuteOfHour(new DateTime(dayPeriodStart).getMinuteOfHour())
					.toDate(),
				new DateTime(planning.getDayPeriod().getFrom())
					.withHourOfDay(new DateTime(dayPeriodEnd).getHourOfDay())
					.withMinuteOfHour(new DateTime(dayPeriodEnd).getMinuteOfHour())
					.toDate()
		) : null;
		
		planningService.update(planning, name, csvFile, period, oralDefenseDuration, oralDefenseInterlude, lunch, dayPeriod, nbMaxOralDefensePerDay, roomsCollection);
		
		return planning;
	}
	
	@RequestMapping("/{id}/participants")
	public Collection<Participant> getParticipants(@PathVariable("id") Integer id) {
		
		session.teacherOnly();
		
		Planning planning = planningService.findById(id);
		
		if (planning != null) {
			return planningService.findParticipants(planning);
		}
		
		return Lists.newArrayList();
		
	}
	
	@RequestMapping("/{id}/participants/unavailabilities")
	public Collection<ParticipantDTO> getParticipantsAndUnavailabilitiesNumber(@PathVariable("id") Integer id) {
		
		session.teacherOnly();
		
		Planning planning = planningService.findById(id);
		
		if (planning != null) {
			return planningService.findParticipantsAndUnavailabilitiesNumber(planning);
		}
		
		return Lists.newArrayList();
		
	}
	
	@RequestMapping("/{id}/priorities")
	public Collection<Priority> getPriorities(@PathVariable("id") Integer id) {
		session.teacherOnly();
		Planning planning = planningService.findById(id);
		
		if (planning != null) {
			return planningService.findPriorities(planning);
		}
		
		return Lists.newArrayList();
		
	}
	
	@RequestMapping(value = "/{id}/priorities/update", method = RequestMethod.POST)
	public Collection<Priority> setPriorities(@PathVariable("id") Integer id, @RequestBody Collection<Priority> priorities) {
		
		Planning planning = planningService.findById(id);
		Validate.notNull(planning);
		
		// check if the current user is the admin of this planning
		session.acceptOnly(planning.getAdmin());
		
		Collection<Priority> results = planningService.updatePriorities(planning, priorities);
		
		return results;
	}
	
	@RequestMapping(value = "/{id}/validate")
	public void validate(@PathVariable("id") Integer id) {

		Planning planning = planningService.findById(id);
		Validate.notNull(planning);
		
		// check if the current user is the admin of this planning
		session.acceptOnly(planning.getAdmin());
		
		planningService.validate(planning);
	}

	@RequestMapping(value = "/{id}/print")
	public void print(@PathVariable("id") Integer id) {

		Planning planning = planningService.findById(id);
		Validate.notNull(planning);

		planningService.validate(planning);
	}

	@RequestMapping(value = "/{id}/delete")
	public void deletePlanning(@PathVariable("id") Integer id){
		System.out.println(id);
		Planning planning = planningService.findById(id);
		System.out.println(planning);
		Validate.notNull(planning);
		
		// check if the current user is the admin of this planning
		session.acceptOnly(planning.getAdmin());
		
		// remove all depsendencies
		// unavailabilities
		unavailabilityService.deleteByPlanning(id);
		// planning
		planningService.delete(planning);
	}
	
	@RequestMapping(value = "/{id}/deleteBackup")
	public void deletePlanningBackup(@PathVariable("id") Integer id) throws IOException{
		FileUtils.cleanDirectory(new File(PERSIST_PATH+"/"+id));
	}

	@RequestMapping(value = "/{id}/duplicate")
	public Integer duplicatePlanning(@PathVariable("id") Integer id) throws IOException{
		Integer draftId = planningService.duplicate(id);
		return draftId;
	}

	@RequestMapping(value = "/{id}/duplicateDraft")
	public Integer duplicateDraft(@PathVariable("id") Integer id) throws IOException{
		Integer draftId = planningService.duplicateDraft(id);
		return draftId;
	}

	@RequestMapping("/{id}/drafts")
	public List<Planning> findDraftsById(@PathVariable("id") Integer id) throws IOException {

		return planningService.findAllDrafts(id);
	}

	@RequestMapping("/switchReference/{idDraft}")
	public void switchReferencePlanning(@PathVariable("idDraft") Integer idDraft) throws IOException{
		planningService.switchReference(idDraft);
	}

	@RequestMapping("/{id}/updateDraft")
	public void updateDraft(@PathVariable("id") Integer id, @RequestBody String modifiedValue){
		Planning planning = planningService.findById(id);
		Map<Integer,List<Creneau>> creneaux_list = planningService.exportJSON(planning);

		JSONArray jsonArray = new JSONArray(modifiedValue);

		for(Map.Entry<Integer, List<Creneau>> entry : creneaux_list.entrySet()){

			Iterator<Creneau> it = entry.getValue().iterator();
			while(it.hasNext()){
				Creneau c = it.next();
				for (int i=0; i<jsonArray.length(); i++) {
					JSONObject item = jsonArray.getJSONObject(i);
					if(c.getStudent().getName().equals(item.getJSONObject("student").get("name"))){
						it.remove();
					}
				}
			}
		}

		PlanningSplitter splitter = new PlanningSplitterImpl();
		List<TimeBox> timeboxes = splitter.execute(planning);
		for (int i=0; i<jsonArray.length(); i++) {
			try {
				System.out.println("test ------------"+ jsonArray.getJSONObject(i).toString());
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				Creneau c = objectMapper.readValue(jsonArray.getJSONObject(i).toString(), Creneau.class);
				TimeBox timebox = timeboxes.get(c.getPeriode());
				c.setHoraire(timebox.getFrom().getDay()+" "+timebox.getFrom().getHours()+" "+ timebox.getFrom().getMinutes());


				creneaux_list.get(c.getPeriode()).add(c);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		for(Map.Entry<Integer, List<Creneau>> entry : creneaux_list.entrySet()){

			for (Creneau c: entry.getValue()) {
				System.out.println("CLE" + entry.getKey() + "   " + c.toString());

			}
		}

		try (
				OutputStream f1 = new FileOutputStream(PERSIST_PATH + "/" + planning.getId() + "/planning.ser");
				OutputStream b1 = new BufferedOutputStream(f1);
				ObjectOutput o1 = new ObjectOutputStream(b1);
		){
			o1.writeObject(creneaux_list);
		}
		catch(IOException ex){
			System.err.println(ex);
		}
	}
	
}
