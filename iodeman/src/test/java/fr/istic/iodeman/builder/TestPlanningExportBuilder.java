package fr.istic.iodeman.builder;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;

import fr.istic.iodeman.model.OralDefense;
import fr.istic.iodeman.model.Participant;
import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.TimeBox;
import fr.istic.iodeman.model.Unavailability;
import fr.istic.iodeman.strategy.AlgoJuryAssignation;
import fr.istic.iodeman.strategy.AlgoPlanningImplV2;
import fr.istic.iodeman.strategy.PlanningDataValidator;
import fr.istic.iodeman.strategy.PlanningExport;
import fr.istic.iodeman.strategy.PlanningSplitter;

public class TestPlanningExportBuilder {

	@InjectMocks
	private PlanningExportBuilder builder;
	
	@Mock
	private PlanningSplitter splitter;
	
	@Mock
	private AlgoPlanningImplV2 algoPlanning;
	
	@Mock
	private AlgoJuryAssignation algoJuryAssignation;
	
	@Mock
	private PlanningExport planningExport;
	
	@Mock
	private PlanningDataValidator validator;
	
	private Planning planning;
	
	@Before
	public void setUp() {
		
		planning = new Planning();
		
		builder = new PlanningExportBuilder(planning);
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testSplit() {
		
		builder.split();
		Mockito.verify(splitter).execute(planning);
		
	}
	
	@Test
	public void testValidate() {
		
		List<Participant> participants = Lists.newArrayList();
		List<TimeBox> timeboxes = Lists.newArrayList();
		
		Mockito.when(splitter.execute(planning)).thenReturn(timeboxes);
		
		builder.split();
		builder.setParticipants(participants);
		builder.validate();
		
		Mockito.verify(validator).configure(planning, participants, timeboxes);
		Mockito.verify(validator).validate();
	}
	
	@Test
	public void testBuild() {
		
		List<Participant> participants = Lists.newArrayList();
		List<TimeBox> timeboxes = Lists.newArrayList();
		List<Unavailability> unavailabilities = Lists.newArrayList();
		
		Mockito.when(splitter.execute(planning)).thenReturn(timeboxes);
		
		builder.split();
		builder.setParticipants(participants);
		builder.setUnavailabilities(unavailabilities);
		builder.build();
		
		Mockito.verify(algoPlanning).configure(planning, participants, timeboxes, unavailabilities);
		Mockito.verify(algoPlanning).execute();
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testBuildWithoutSplit() {
		
		builder.build();
	}
	
	@Test
	public void testBuildToExcel() throws Exception {
		
		List<Participant> participants = Lists.newArrayList();
		List<TimeBox> timeboxes = Lists.newArrayList();
		List<Unavailability> unavailabilities = Lists.newArrayList();
		List<OralDefense> oralDefenses = Lists.newArrayList();
		
		Mockito.when(splitter.execute(planning)).thenReturn(timeboxes);
		Mockito.when(algoPlanning.execute()).thenReturn(oralDefenses);
		
		builder.setParticipants(participants);
		builder.setUnavailabilities(unavailabilities);
		builder.split().build();
		builder.toExcel();

		Mockito.verify(planningExport).configure(timeboxes);
		Mockito.verify(planningExport).execute(oralDefenses);
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testExcelWithoutBuild() throws Exception {
		
		List<Participant> participants = Lists.newArrayList();
		List<TimeBox> timeboxes = Lists.newArrayList();
		List<Unavailability> unavailabilities = Lists.newArrayList();
		List<OralDefense> oralDefenses = Lists.newArrayList();
		
		Mockito.when(splitter.execute(planning)).thenReturn(timeboxes);
		Mockito.when(algoPlanning.execute()).thenReturn(oralDefenses);
		
		builder.setParticipants(participants);
		builder.setUnavailabilities(unavailabilities);
		builder.split();
		builder.toExcel();
		
	}
	
	
	
}
