package fr.istic.iodeman.strategy;

import java.io.File;
import java.util.Collection;

import fr.istic.iodeman.model.OralDefense;
import fr.istic.iodeman.model.TimeBox;

public interface PlanningExport {
	public void configure(Collection<TimeBox> timeboxes);
	public File execute(Collection<OralDefense> oralDefenses) throws Exception ;
}
