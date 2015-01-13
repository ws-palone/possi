package fr.istic.iodeman.strategy;

import java.util.Collection;

import fr.istic.iodeman.model.OralDefense;

public interface PlanningExport {
	public void execute(Collection<OralDefense> oralDefenses);
}
