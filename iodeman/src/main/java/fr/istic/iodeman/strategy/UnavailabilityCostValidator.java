package fr.istic.iodeman.strategy;

import java.util.Collection;

import fr.istic.iodeman.model.OralDefense;
import fr.istic.iodeman.model.Priority;
import fr.istic.iodeman.model.Unavailability;

public interface UnavailabilityCostValidator {
	
	public void configure(Collection<Priority> priorities);
	
	public int execute(Collection<OralDefense> oralDefenses, Collection<Unavailability> unavailabilities);
	
	public int getCost();
	
	public Collection<Unavailability> getNotRespectedUnavailabilities();

}
