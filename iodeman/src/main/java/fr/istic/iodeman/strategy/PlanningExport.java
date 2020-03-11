package fr.istic.iodeman.strategy;

import fr.istic.iodeman.model.OralDefense;
import fr.istic.iodeman.model.TimeBox;

import java.io.File;
import java.util.Collection;

public interface PlanningExport {
	public void configure(Collection<TimeBox> timeboxes);
	public File execute(Collection<OralDefense> oralDefenses) throws Exception ;
}
