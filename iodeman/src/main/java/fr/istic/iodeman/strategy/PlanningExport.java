package fr.istic.iodeman.strategy;

import java.util.Collection;

import fr.istic.iodeman.model.OralDefense;
import fr.istic.iodeman.model.TimeBox;

public interface PlanningExport {
	public void configure(Collection<TimeBox> timeboxes);
	public void execute(Collection<OralDefense> oralDefenses) throws Exception ;
}
