package fr.istic.iodeman.strategy;

import java.util.Collection;

import fr.istic.iodeman.model.OralDefense;
import fr.istic.iodeman.model.Unavailability;

public interface AlgoJuryAssignation {

	public void configure(Collection<OralDefense> oralDefenses, Collection<Unavailability> unavailabilities);
	
	public Collection<OralDefense> execute();
	
}
