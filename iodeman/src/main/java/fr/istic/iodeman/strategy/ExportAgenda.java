package fr.istic.iodeman.strategy;

import java.util.Collection;

import fr.istic.iodeman.dto.AgendaDTO;
import fr.istic.iodeman.model.TimeBox;
import fr.istic.iodeman.model.Unavailability;

public interface ExportAgenda {
	Collection<AgendaDTO> execute(Collection<TimeBox> timeboxes, Collection<Unavailability> Unavailabilities);
}
