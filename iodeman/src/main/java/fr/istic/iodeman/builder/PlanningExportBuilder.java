package fr.istic.iodeman.builder;


import fr.istic.iodeman.models.*;
import fr.istic.iodeman.repositories.ColorRepository;
import fr.istic.iodeman.resolver.PersonMailResolver;
import fr.istic.iodeman.strategy.*;
import fr.istic.possijar.AlgoPlanningImplV4;
import fr.istic.possijar.Creneau;
import fr.istic.possijar.Enseignant;
import org.apache.commons.lang.Validate;

import java.util.*;
import java.util.stream.Collectors;

public class PlanningExportBuilder {

	private PlanningSplitter splitter = new PlanningSplitterImpl();
	private AlgoPlanningImplV4 algoPlanning_new = new AlgoPlanningImplV4();
	private Planning planning;
	private Collection<Unavailability> unavailabilities;
	private Collection<OralDefense> oralDefenses;
	private List<TimeBox> timeboxes;

	private final ColorRepository colorRepository;

	public PlanningExportBuilder(ColorRepository colorRepository) {
		this.colorRepository = colorRepository;
	}

	public void setPlanning(Planning planning) {
		Validate.notNull(planning);
		this.planning = planning;
	}

	public PlanningExportBuilder setUnavailabilities(Collection<Unavailability> unavailabilities) {
		this.unavailabilities = new ArrayList<>(unavailabilities);
		return this;
	}

	public PlanningExportBuilder setOralDefenses(Collection<OralDefense> oralDefenses) {
		this.oralDefenses = new ArrayList<>(oralDefenses);
		return this;
	}

	public PlanningExportBuilder split() {
		timeboxes = splitter.execute(planning).getTimeBoxes();
		return this;
	}

	public PlanningExportBuilder build() {
		System.out.println("PlanningExportBuilder build");
		Validate.notNull(timeboxes);
		Validate.notNull(unavailabilities);
		Validate.notNull(oralDefenses);
		algoPlanning_new.deserialize(planning.getId());
		algoPlanning_new.configure(planning, oralDefenses, timeboxes, splitter.getTimeBoxesWithoutLunchBreak(), splitter.getLunchBreakTimeBoxes(), unavailabilities);
		algoPlanning_new.execute();
		algoPlanning_new.serialize(planning.getId());
		return this;
	}
	public Collection<OralDefense> getOralDefenses(PersonMailResolver personMailResolver) {
		Collection<List<Creneau>> creneaux = this.algoPlanning_new.getPlanning().values().stream().filter(c -> c.size() > 0).collect(Collectors.toList());
		Collection<OralDefense> oralDefenses = new ArrayList<>();
		List<Room> roomsSelected = new ArrayList<>(this.planning.getRooms());
		roomsSelected.sort(Comparator.comparing(Room::getId));
		for (List<Creneau> creneauList : creneaux) {
			for (Creneau creneau : creneauList) {

				Iterator<OralDefense> iterator = this.oralDefenses.iterator();
				boolean found = false;
				while (iterator.hasNext() && !found) {
					OralDefense oralDefense = iterator.next();
					if (oralDefense.getStudent().getEmail().equals(creneau.getStudent().getName())) {
						oralDefense.setTimeBox(this.timeboxes.get(creneau.getPeriode()));
						oralDefense.setNumber(creneau.getNumero());
						oralDefense.setColor(colorRepository.findById(creneau.getCouleur()).get());
						oralDefense.setRoom(roomsSelected.get(creneau.getSalle() - 1));
						Enseignant secondTeacher = creneau.getCandide();
						if (secondTeacher != null)
							oralDefense.setSecondTeacher(personMailResolver.resolve(secondTeacher.getName()));
						else
							oralDefense.setSecondTeacher(null);
						found = true;
					}
					oralDefenses.add(oralDefense);
				}
			}
		}
		return oralDefenses;
	}

	public Collection<OralDefense> updatePlanning(List<Unavailability> unavailabilities) {
		List<Creneau> creneauUpdated = algoPlanning_new.updateCrenaux(unavailabilities, this.timeboxes, this.planning.getId());
		List<Room> roomsSelected = new ArrayList<>(this.planning.getRooms());
		roomsSelected.sort(Comparator.comparing(Room::getId));
		List<OralDefense> newOralDefenses = new ArrayList<>();
		for (Creneau c : creneauUpdated) {
			boolean found = false;
			Iterator<OralDefense> iterator = planning.getOralDefenses().iterator();
			while(iterator.hasNext() && !found) {
				OralDefense oralDefense = iterator.next();
				if (oralDefense.getNumber() == c.getNumero()) {
					oralDefense.setTimeBox(this.timeboxes.get(c.getPeriode()));
					oralDefense.setRoom(roomsSelected.get(c.getSalle() - 1));
					newOralDefenses.add(oralDefense);
					found = true;
				}
			}
		}
		return newOralDefenses;
	}
}
