package fr.istic.iodeman.strategy;

import java.util.ArrayList;
import java.util.Collection;

import fr.istic.iodeman.dto.AgendaDTO;
import fr.istic.iodeman.model.TimeBox;
import fr.istic.iodeman.model.Unavailability;

public class ExportJsonAgenda implements ExportAgenda{

	@Override
	public Collection<AgendaDTO> execute(Collection<TimeBox> timeboxes, Collection<Unavailability> unavailabilities) {
		Collection<AgendaDTO> agendaDtos = new ArrayList<AgendaDTO>();
		
		for(TimeBox t : timeboxes){
			AgendaDTO a = new AgendaDTO();
			a.setTimebox(t);
			agendaDtos.add(a);
		}
		
		return agendaDtos;
	}

}
