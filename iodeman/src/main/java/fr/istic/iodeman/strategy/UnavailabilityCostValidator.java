package fr.istic.iodeman.strategy;

import fr.istic.iodeman.model.OralDefense;
import fr.istic.iodeman.model.Priority;
import fr.istic.iodeman.model.Unavailability;

import java.util.Collection;

public interface UnavailabilityCostValidator {
	
	public void configure(Collection<Priority> priorities);
	
	public int execute(Collection<OralDefense> oralDefenses, Collection<Unavailability> unavailabilities);
	
	public int getCost();
	
	public Collection<Unavailability> getNotRespectedUnavailabilities();

}
