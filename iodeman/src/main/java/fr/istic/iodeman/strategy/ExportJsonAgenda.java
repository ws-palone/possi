package fr.istic.iodeman.strategy;

import java.util.ArrayList;
import java.util.Collection;

import org.joda.time.DateTime;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import fr.istic.iodeman.dto.AgendaDTO;
import fr.istic.iodeman.model.TimeBox;
import fr.istic.iodeman.model.Unavailability;
import fr.istic.iodeman.utils.AlgoPlanningUtils;

public class ExportJsonAgenda implements ExportAgenda{

	@Override
	public Collection<AgendaDTO> execute(Collection<TimeBox> timeboxes, Collection<Unavailability> unavailabilities) {
		Collection<AgendaDTO> agendaDtos = new ArrayList<AgendaDTO>();
		
		// saving of the first day in order to know how the day changes
		int savedDayOfyear = 0;
		
		// to know if is the first iteration
		Boolean firstPass = true;
		
		for(TimeBox t : Lists.newArrayList(timeboxes)){
			// datetime objects
			DateTime from = new DateTime(t.getFrom());
			DateTime to = new DateTime(t.getTo());			
			
			// the saving of the first pass timebox
			// In order to avoid the iteration of other days timeboxes
			if (firstPass){
				savedDayOfyear = from.getDayOfYear();
				firstPass = false;
			} else if (savedDayOfyear != from.getDayOfYear()){
				break;
			}					
						
			// line header
			String line = from.toString("HH")+"h"+from.toString("mm");
			line += " - "+to.toString("HH")+"h"+to.toString("mm");
			
			// agenda DTO
			AgendaDTO a = new AgendaDTO();
			a.setLine(line);
			for(TimeBox timeBox : getTimeboxesForDays(t, timeboxes)){
				
				// line header
				DateTime currentfrom = new DateTime(timeBox.getFrom());
				
				// checked
				// is not available
				Boolean checked = true;
				if (AlgoPlanningUtils.isAvailable(unavailabilities, timeBox)) checked = false;
				
				a.addDayItem(currentfrom.toString("dd/MM"), checked, timeBox);
			}
						
			agendaDtos.add(a);
		}
		
		return agendaDtos;
	}
	
	private Collection<TimeBox> getTimeboxesForDays(final TimeBox timebox, Collection<TimeBox> timeboxes){
		Collection<TimeBox> filteredTimeboxes = null;
		
		filteredTimeboxes = Collections2.filter(timeboxes, new Predicate<TimeBox>() {

			@Override
			public boolean apply(TimeBox comparedTb) {
				DateTime t = new DateTime(timebox.getFrom());
				DateTime ctb = new DateTime(comparedTb.getFrom());
				return (t.getMinuteOfDay() == ctb.getMinuteOfDay()) ;
			}
			
		});
		
		
		return filteredTimeboxes;
	}

}
