package fr.istic.iodeman.builder;

import istic.m2miage.possicat.AlgoPlanningImplV3;

import java.io.File;
import java.util.Collection;

import org.apache.commons.lang.Validate;

import com.google.common.collect.Lists;

import fr.istic.iodeman.model.OralDefense;
import fr.istic.iodeman.model.Participant;
import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.TimeBox;
import fr.istic.iodeman.model.Unavailability;
import fr.istic.iodeman.strategy.AlgoJuryAssignation;
import fr.istic.iodeman.strategy.AlgoJuryAssignationImpl;
import fr.istic.iodeman.strategy.AlgoPlanningImplV2;
import fr.istic.iodeman.strategy.PlanningDataValidator;
import fr.istic.iodeman.strategy.PlanningDataValidatorImpl;
import fr.istic.iodeman.strategy.PlanningExcelExport;
import fr.istic.iodeman.strategy.PlanningExport;
import fr.istic.iodeman.strategy.PlanningSplitter;
import fr.istic.iodeman.strategy.PlanningSplitterImpl;

public class PlanningExportBuilder {

	private PlanningSplitter splitter = new PlanningSplitterImpl();
	//private AlgoPlanningImplV2 algoPlanning = new AlgoPlanningImplV2();
	private AlgoPlanningImplV3 algoPlanning_new = new AlgoPlanningImplV3();
	private AlgoJuryAssignation algoJuryAssignation = new AlgoJuryAssignationImpl();
	private PlanningExport planningExport = new PlanningExcelExport();
	private PlanningDataValidator validator = new PlanningDataValidatorImpl();
	
	private Planning planning;
	private Collection<Unavailability> unavailabilities;
	private Collection<Participant> participants;
	
	private Collection<TimeBox> timeboxes;
	private Collection<OralDefense> oralDefenses;
	
	public PlanningExportBuilder(Planning p) {
		Validate.notNull(p);
		planning = p;
	}
	
	public PlanningExportBuilder setUnavailabilities(Collection<Unavailability> unavailabilities) {
		this.unavailabilities = Lists.newArrayList(unavailabilities);
		return this;
	}
	
	public PlanningExportBuilder setParticipants(Collection<Participant> participants) {
		this.participants = Lists.newArrayList(participants);
		return this;
	}
	
	public PlanningExportBuilder split() {
		timeboxes = splitter.execute(planning);
		return this;
	}
	
	public PlanningExportBuilder validate() {
		this.validator.configure(planning, participants, timeboxes);
		this.validator.validate();
		return this;
	}
	
	public PlanningExportBuilder build() {
		Validate.notNull(timeboxes);
		Validate.notNull(unavailabilities);
		Validate.notNull(participants);
		//algoPlanning.configure(planning, participants, timeboxes, unavailabilities);
		algoPlanning_new.configure(planning, participants, timeboxes, unavailabilities);
		algoPlanning_new.execute();
		//oralDefenses = algoPlanning.execute();
		//algoJuryAssignation.configure(oralDefenses, unavailabilities);
		//oralDefenses = algoJuryAssignation.execute();
		return this;
	}
	
	public File toCSV() throws Exception {
		return algoPlanning_new.getFile();
	}
	
	/*public File toExcel() throws Exception {
		Validate.notNull(oralDefenses);
		planningExport.configure(timeboxes);
		return planningExport.execute(oralDefenses);
	}*/
	
	
	public Collection<TimeBox> getTimeboxes() {
		return this.timeboxes;
	}
	
	public Collection<OralDefense> getOralDefenses() {
		return this.oralDefenses;
	}
	
}
