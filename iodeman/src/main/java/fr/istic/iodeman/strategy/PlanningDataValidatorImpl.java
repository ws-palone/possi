package fr.istic.iodeman.strategy;

import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import fr.istic.iodeman.models.OralDefense;
import fr.istic.iodeman.models.Planning;
import fr.istic.iodeman.models.Room;
import fr.istic.iodeman.models.TimeBox;
import org.apache.commons.lang.Validate;
import org.joda.time.DateTime;

import java.util.Collection;
import java.util.List;

public class PlanningDataValidatorImpl implements PlanningDataValidator{

	private Planning planning;
	private Collection<OralDefense> oralDefenses;
	private Collection<TimeBox> timeboxes;
	
	@Override
	public void configure(Planning planning,
			Collection<OralDefense> participants, Collection<TimeBox> timeboxes) {
		
		this.planning = planning;
		this.oralDefenses = participants;
		this.timeboxes = timeboxes;
	}

	@Override
	public void validate() {
		
		Validate.notNull(planning);
		
		// verify that we have rooms
		Validate.notNull(planning.getRooms());
		Validate.notEmpty(planning.getRooms());
				
		// verify that we have timeboxes
		Validate.notNull(timeboxes);
		Validate.notEmpty(timeboxes);
				
		// verify that we have participants
		Validate.notNull(oralDefenses);
		Validate.notEmpty(oralDefenses);
				
		// verify that we have enough timeboxes and rooms to create an oral defense for each participant
		Validate.isTrue(timeboxes.size() * planning.getRooms().size() >= oralDefenses.size());
		
		List<Room> rooms = Lists.newArrayList(planning.getRooms());
		
		Integer nbMaxOralDefensesPerDay = planning.getNbMaxOralDefensePerDay();
		if (nbMaxOralDefensesPerDay == null) nbMaxOralDefensesPerDay = 0;
		
		// get the list of the days in the timeboxes
		List<Integer> days = ImmutableSet.copyOf(
				Collections2.transform(timeboxes, tb -> new DateTime(tb.getFrom()).dayOfYear().get())
		).asList();
		
		int nbBoxes = 0;
		
		if (nbMaxOralDefensesPerDay < 1) {
			
			nbBoxes = timeboxes.size() * rooms.size();
		
		}else{
		
			for (final Integer day : days) {
				
				Collection<TimeBox> timeboxesOfDay = Collections2.filter(timeboxes, tb -> (day == new DateTime(tb.getFrom()).dayOfYear().get()));
				
				int nb = timeboxesOfDay.size() * rooms.size();
				
				nbBoxes += (nb > nbMaxOralDefensesPerDay) ? nbMaxOralDefensesPerDay : nb;

			}
			
		}
		
		// verify that be have enough timeboxes for all the participants
		Validate.isTrue(nbBoxes >= oralDefenses.size());

	}

	
	
}
