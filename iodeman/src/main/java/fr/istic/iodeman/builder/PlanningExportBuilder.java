package fr.istic.iodeman.builder;


import com.google.common.collect.Lists;
import fr.istic.iodeman.model.*;
import fr.istic.iodeman.strategy.*;
import fr.istic.possijar.AlgoPlanningImplV3;
import fr.istic.possijar.Creneau;
import org.apache.commons.lang.Validate;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

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
		algoPlanning_new.deserialize(planning.getId());
		algoPlanning_new.configure(planning, participants, timeboxes, unavailabilities);
		algoPlanning_new.execute();
		algoPlanning_new.serialize(planning.getId());
		//oralDefenses = algoPlanning.execute();
		//algoJuryAssignation.configure(oralDefenses, unavailabilities);
		//oralDefenses = algoJuryAssignation.execute();
		return this;
	}
	
	public File toCSV() throws Exception {
		return algoPlanning_new.getFile();
	}

	public File toXls() throws Exception {
		int nbPeriodesParJour = algoPlanning_new.getNbPeriodesParJour();
		List<String> sallesSelectionnees = algoPlanning_new.getSallesSelectionnees();
		Map<Integer, List<Creneau>> p = algoPlanning_new.getPlanning();
		Set<Integer> periodes = p.keySet();
		List<TimeBox> listTimeboxes;
		if (timeboxes instanceof List)
			listTimeboxes = (List<TimeBox>)timeboxes;
		else
			listTimeboxes = new ArrayList<>(timeboxes);

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet planningSheet = workbook.createSheet("Planning");

		//creating a custom palette for the workbook
		HSSFPalette palette = workbook.getCustomPalette();
		//replacing the standard green
		palette.setColorAtIndex(HSSFColor.GREEN.index,
				(byte) 204,  //RGB red (0-255)
				(byte) 255,  //RGB green
				(byte) 204   //RGB blue
		);
		//replacing the standard blue
		palette.setColorAtIndex(HSSFColor.BLUE.index, (byte) 204, (byte) 236, (byte) 255);
		//replacing the standard tan
		palette.setColorAtIndex(HSSFColor.TAN.index, (byte) 255, (byte) 255, (byte) 204);

		// DATE STYLE
		HSSFCellStyle dateStyle = workbook.createCellStyle();
		dateStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
		dateStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		// SALLE STYLE
		HSSFCellStyle salleStyle = workbook.createCellStyle();
		salleStyle.setFillForegroundColor(IndexedColors.TAN.getIndex());
		salleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		int rowIndex = 0;
		int cellIndex = 0;
		HSSFRow row;
		// Pour chaque creneau
		for(int periode : periodes){
			// Si nouvelle journee
			if(periode%nbPeriodesParJour == 0){
				if(periode > 0 ) {
					planningSheet.createRow(rowIndex++);
					planningSheet.createRow(rowIndex++);
				}
				row = planningSheet.createRow(rowIndex);
				cellIndex = 0;
				planningSheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, cellIndex, 4));
				SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd MMMM yyyy", Locale.FRANCE);
				String date = sdf.format(listTimeboxes.get(periode).getFrom());
				HSSFCell cell = row.createCell(cellIndex);
				cell.setCellValue(date);
				cell.setCellStyle(dateStyle);
				CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
				rowIndex++;

				int salleNumber = 0;
				for(String salle : sallesSelectionnees){
					if(salleNumber > 0){
						planningSheet.createRow(rowIndex++);
					}

					cellIndex = 1;
					// SALLE
					row = planningSheet.createRow(rowIndex);
					planningSheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, cellIndex, 4));
					(cell = row.createCell(cellIndex)).setCellValue(salle);
					cell.setCellStyle(salleStyle);
					CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
					rowIndex++;

					// HEADER
					row = planningSheet.createRow(rowIndex++);
					(cell = row.createCell(cellIndex++)).setCellValue("Etudiant");
					CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
					(cell = row.createCell(cellIndex++)).setCellValue("Enseignant \"suiveur\"");
					CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
					(cell = row.createCell(cellIndex++)).setCellValue("Enseignant co-jury");
					CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
					(cell = row.createCell(cellIndex++)).setCellValue("Tuteur entreprise");
					CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
					salleNumber++;
				}
			}

			// Ajout de la correspondance au creneau
			List<Creneau> creneaux = p.get(periode);
			cellIndex = 0;
			if(creneaux.size()>0) {
				row = planningSheet.createRow(rowIndex++);
				row.createCell(cellIndex++).setCellValue(creneaux.get(0).getHoraire());
				row.createCell(cellIndex++).setCellValue(creneaux.get(0).getStudent().getName());
				row.createCell(cellIndex++).setCellValue(creneaux.get(0).getTuteur().getName());
				row.createCell(cellIndex++).setCellValue(creneaux.get(0).getEnseignant().getName());
				row.createCell(cellIndex++).setCellValue(creneaux.get(0).getCandide().getName());
			}
		}

		// Rearrange columns size
		for(int i=0; i < planningSheet.getPhysicalNumberOfRows()-1; i++) {
			row = planningSheet.getRow(i);
			Iterator<Cell> cellIterator = row.cellIterator();
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				int columnIndex = cell.getColumnIndex();
				planningSheet.autoSizeColumn(columnIndex);
			}
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date now = new Date();
		File f = new File("Soutenances_"+sdf.format(now)+".xls");
		FileOutputStream stream = new FileOutputStream(f);
		workbook.write(stream);
		workbook.close();
		return f;
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
