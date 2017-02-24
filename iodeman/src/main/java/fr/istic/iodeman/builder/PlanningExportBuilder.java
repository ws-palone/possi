package fr.istic.iodeman.builder;


import com.google.common.collect.Lists;
import fr.istic.iodeman.model.*;
import fr.istic.iodeman.strategy.*;
import fr.istic.iodeman.utils.AlgoPlanningUtils;
import fr.istic.possijar.AlgoPlanningImplV3;
import fr.istic.possijar.Creneau;
import org.apache.commons.lang.Validate;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.usermodel.HeaderFooter;
import org.apache.poi.ss.usermodel.*;
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

	private ArrayList<Short> defcouleur = new ArrayList<Short>()
	{{
        add(IndexedColors.GREY_25_PERCENT.getIndex());
        add(IndexedColors.YELLOW.getIndex());
        add(IndexedColors.PALE_BLUE.getIndex());
        add(IndexedColors.LIGHT_GREEN.getIndex());
        add(IndexedColors.LIGHT_ORANGE.getIndex());
        add(IndexedColors.WHITE.getIndex());
        add(IndexedColors.LIGHT_TURQUOISE.getIndex());
        add(IndexedColors.LIGHT_YELLOW.getIndex());
        add(IndexedColors.CORAL.getIndex());
        add(IndexedColors.LEMON_CHIFFON.getIndex());
    }};

	private Map<String,HSSFCellStyle> couleurParProf = new HashMap<>();
	
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
		System.out.println("PlanningExportBuilder build");
		Validate.notNull(timeboxes);
		Validate.notNull(unavailabilities);
		Validate.notNull(participants);
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

		Validate.notNull(timeboxes);
		Validate.notNull(participants);
		algoPlanning_new.deserialize(planning.getId());
		algoPlanning_new.configure(planning, participants, timeboxes, unavailabilities);

		int nbPeriodesParJour = algoPlanning_new.getNbPeriodesParJour();
		List<String> sallesSelectionnees = new ArrayList<>();

		List<Room> rooms = new ArrayList<>(planning.getRooms());
		Collections.sort(rooms, Comparator.comparing(left -> ((Integer) left.getId())));
		for(Room room: rooms) { sallesSelectionnees.add(room.getName());}
		Map<Integer, List<Creneau>> p = algoPlanning_new.getPlanning();
		Set<Integer> periodes = p.keySet();
		List<TimeBox> listTimeboxes;
		if (timeboxes instanceof List)
			listTimeboxes = (List<TimeBox>)timeboxes;
		else
			listTimeboxes = new ArrayList<>(timeboxes);

		Planning tmp = new Planning(planning);
		tmp.setPeriod(planning.getDayPeriod());
		PlanningSplitter splitter = new PlanningSplitterImpl();
		List<TimeBox> emptyDayTBx = splitter.execute(tmp);
		List<Creneau> emptyDay = new ArrayList<>();
		for(int i = 0; i < emptyDayTBx.size(); i++){
			Creneau c = new Creneau(i, null, null, null, null);
			c.setHoraire(emptyDayTBx.get(i).getFrom().getDate()+
					" " + emptyDayTBx.get(i).getFrom().getHours() +
					" " + emptyDayTBx.get(i).getFrom().getMinutes());
			emptyDay.add(c);
		}

		int rowParPage = 26;
		int rowCount = 1;

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet planningSheet = workbook.createSheet("Planning");

		HSSFFont font = workbook.createFont();
		font.setFontHeight((short)(13*20));

		planningSheet.setDefaultRowHeightInPoints((short) (font.getFontHeightInPoints() + 4));

		// DEFAULT STYLE
		HSSFCellStyle defaultStyle = workbook.createCellStyle();
		defaultStyle.setFont(font);
		defaultStyle.setWrapText(true);
		defaultStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		// DATE STYLE
		HSSFCellStyle dateStyle = workbook.createCellStyle();
		dateStyle.setFont(font);
		dateStyle.setWrapText(true);
		dateStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		dateStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		dateStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		// SALLE STYLE
		HSSFCellStyle salleStyle = workbook.createCellStyle();
		salleStyle.setFont(font);
		salleStyle.setWrapText(true);
		salleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		salleStyle.setFillForegroundColor(IndexedColors.GOLD.getIndex());
		salleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		int rowIndex = 0;
		int cellIndex = 0;
		HSSFRow row;
		HSSFCell cell;
		List<List<Creneau>> days = new ArrayList<>();
		List<Creneau> day = new ArrayList<>();
		// Pour chaque creneau
		for(int periode : periodes) {
			// Si nouvelle journee
			if (periode % nbPeriodesParJour == 0) {
				if (periode > 0) {
					days.add(day);
					day = new ArrayList<>();
				}
			}
			// Afficher date
			day.addAll(p.get(periode));
		}
		days.add(day);

		// Ajout du titre
		row = planningSheet.createRow(rowIndex);
		row.setHeightInPoints(planningSheet.getDefaultRowHeightInPoints());
		planningSheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, cellIndex, 4));
		cell = row.createCell(cellIndex);
		cell.setCellValue(planning.getName());
		cell.setCellStyle(defaultStyle);
		CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
		rowIndex++;
		rowCount++;

		HSSFCellStyle colorStyle;
		int cpt = 0;
		// Pour chaque journée
		for (int i = 0; i < days.size(); i++) {
			List<Creneau> d = days.get(i);
			if(!d.isEmpty()) {
				// Ajout des jours restants
				while(i > 0 && rowCount > 2 && cpt < emptyDay.size()){
					cellIndex=0;
					row = planningSheet.createRow(rowIndex++);
					rowCount++;
					row.setHeightInPoints(planningSheet.getDefaultRowHeightInPoints());
					// Horaire
					(cell = row.createCell(cellIndex++)).setCellValue(emptyDay.get(cpt).getHoraire());
					cell.setCellStyle(defaultStyle);
					cpt++;
				}

				// on passe le jour sur la page suivante
				while(i > 0 && rowCount > 2 && rowCount % rowParPage != 1){
					planningSheet.createRow(rowIndex++);
					rowCount++;
				}

				// Afficher date
				row = planningSheet.createRow(rowIndex);
				row.setHeightInPoints(planningSheet.getDefaultRowHeightInPoints());
				cellIndex = 0;
				planningSheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, cellIndex, 4));
				SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd MMMM yyyy", Locale.FRANCE);
				String date = sdf.format(listTimeboxes.get(d.get(0).getPeriode()).getFrom());
				cell = row.createCell(cellIndex);
				cell.setCellValue(date);
				cell.setCellStyle(dateStyle);
				CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
				rowIndex++;
				rowCount++;

				//ordonner la liste par salle
				Collections.sort(d, (o1, o2) -> {
					if(o1.getSalle()>o2.getSalle())
						return 1;
					else if(o1.getSalle()<o2.getSalle())
						return -1;
					return 0;
				});

				int salleEnCours = -1;
				cpt = 0;
				for(Creneau c : d){
					if(salleEnCours != c.getSalle()){
						// Ajout des creneaux vides
						while(salleEnCours != -1 && cpt < emptyDay.size()){
							cellIndex=0;
							row = planningSheet.createRow(rowIndex++);
							rowCount++;
							row.setHeightInPoints(planningSheet.getDefaultRowHeightInPoints());
							// Horaire
							(cell = row.createCell(cellIndex++)).setCellValue(emptyDay.get(cpt).getHoraire());
							cell.setCellStyle(defaultStyle);
							cpt++;
						}

						if(salleEnCours != -1 && rowCount % rowParPage > 1){
							planningSheet.createRow(rowIndex++);
							rowCount++;
						}
						while(rowCount % rowParPage >= (rowParPage-2) || rowCount % rowParPage == 0){
							planningSheet.createRow(rowIndex++);
							rowCount++;
						}
						salleEnCours = c.getSalle();
						cellIndex = 1;
						// SALLE
						row = planningSheet.createRow(rowIndex);
						row.setHeightInPoints(planningSheet.getDefaultRowHeightInPoints());
						planningSheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, cellIndex, 4));
						(cell = row.createCell(cellIndex)).setCellValue(sallesSelectionnees.get(salleEnCours-1));
						cell.setCellStyle(salleStyle);
						CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
						rowIndex++;
						rowCount++;

						// HEADER
						row = planningSheet.createRow(rowIndex++);
						rowCount++;
						row.setHeightInPoints(planningSheet.getDefaultRowHeightInPoints());
						(cell = row.createCell(cellIndex++)).setCellValue("Etudiant");
						cell.setCellStyle(defaultStyle);
						CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);

						(cell = row.createCell(cellIndex++)).setCellValue("Enseignant \"suiveur\"");
						cell.setCellStyle(defaultStyle);
						CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);

						(cell = row.createCell(cellIndex++)).setCellValue("Enseignant co-jury");
						cell.setCellStyle(defaultStyle);
						CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);

						(cell = row.createCell(cellIndex++)).setCellValue("Tuteur entreprise");
						cell.setCellStyle(defaultStyle);
						CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);

						cpt = 0;
					}

					// Ajout des creneaux vides
					while(c.getPeriode() % nbPeriodesParJour > emptyDay.get(cpt).getPeriode()){
						cellIndex=0;
						row = planningSheet.createRow(rowIndex++);
						rowCount++;
						row.setHeightInPoints(planningSheet.getDefaultRowHeightInPoints());
						// Horaire
						(cell = row.createCell(cellIndex++)).setCellValue(emptyDay.get(cpt).getHoraire());
						cell.setCellStyle(defaultStyle);
						cpt++;
					}
					cpt++; // incrémente pour la soutenance qui va etre ajoutée ensuite

					// Soutenances
					cellIndex=0;
					row = planningSheet.createRow(rowIndex++);
					rowCount += 2;
					row.setHeightInPoints(2*planningSheet.getDefaultRowHeightInPoints());

					// Horaire
					(cell = row.createCell(cellIndex++)).setCellValue(c.getHoraire());
					cell.setCellStyle(defaultStyle);

					// Etudiant
					cell = row.createCell(cellIndex++);
					if(c.getStudent() != null) cell.setCellValue(AlgoPlanningUtils.emailToName(c.getStudent().getName()));
					cell.setCellStyle(defaultStyle);

					// Prof 1
					cell = row.createCell(cellIndex++);
					if(c.getEnseignant() != null) {
						cell.setCellValue(AlgoPlanningUtils.emailToName(c.getEnseignant().getName()));
						colorStyle = getColor(workbook,c.getEnseignant().getName());
						colorStyle.setVerticalAlignment(VerticalAlignment.CENTER);
						colorStyle.setFont(font);
						cell.setCellStyle(colorStyle);
					}

					// Prof 2
					cell = row.createCell(cellIndex++);
					if(c.getCandide() != null) {
						cell.setCellValue(AlgoPlanningUtils.emailToName(c.getCandide().getName()));
						colorStyle = getColor(workbook,c.getCandide().getName());
						colorStyle.setVerticalAlignment(VerticalAlignment.CENTER);
						colorStyle.setFont(font);
						cell.setCellStyle(colorStyle);
					}

					// Tuteur
					cell = row.createCell(cellIndex++);
					if(c.getTuteur() != null) cell.setCellValue(c.getTuteur().getName());
					cell.setCellStyle(defaultStyle);
				}
			}
		}
		// Ajout des creneaux vides
		while(cpt < emptyDay.size()){
			cellIndex=0;
			row = planningSheet.createRow(rowIndex++);
			rowCount++;
			row.setHeightInPoints(planningSheet.getDefaultRowHeightInPoints());
			// Horaire
			(cell = row.createCell(cellIndex++)).setCellValue(emptyDay.get(cpt).getHoraire());
			cell.setCellStyle(defaultStyle);
			cpt++;
		}

		// Soutenances posant problèmes
		List<Creneau> impossibleAInserer = algoPlanning_new.getImpossibleAInserer();
		cellIndex = 0;
		// on passe sur la page suivante
		while(rowCount > 2 && rowCount % rowParPage != 1){
			planningSheet.createRow(rowIndex++);
			rowCount++;
		}
		row = planningSheet.createRow(rowIndex);
		row.setHeightInPoints(planningSheet.getDefaultRowHeightInPoints());
		planningSheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 4));
		(cell = row.createCell(cellIndex++)).setCellValue("Soutenances qui posent problèmes : ");
		cell.setCellStyle(defaultStyle);
		rowIndex++;

		row = planningSheet.createRow(rowIndex++);
		row.setHeightInPoints(planningSheet.getDefaultRowHeightInPoints());
		(cell = row.createCell(cellIndex++)).setCellValue("Etudiant");
		cell.setCellStyle(defaultStyle);
		CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
		(cell = row.createCell(cellIndex++)).setCellValue("Enseignant \"suiveur\"");
		cell.setCellStyle(defaultStyle);
		CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
		(cell = row.createCell(cellIndex++)).setCellValue("Enseignant co-jury");
		cell.setCellStyle(defaultStyle);
		CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
		(cell = row.createCell(cellIndex++)).setCellValue("Tuteur entreprise");
		cell.setCellStyle(defaultStyle);
		CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
		for(Creneau c : impossibleAInserer) {
			cellIndex = 1;
			row = planningSheet.createRow(rowIndex++);
			row.setHeightInPoints(2*planningSheet.getDefaultRowHeightInPoints());
			cell = row.createCell(cellIndex++);
			cell.setCellStyle(defaultStyle);
			if(c.getStudent() != null) cell.setCellValue(AlgoPlanningUtils.emailToName(c.getStudent().getName()));
			cell = row.createCell(cellIndex++);
			if(c.getEnseignant() != null){
				cell.setCellValue(AlgoPlanningUtils.emailToName(c.getEnseignant().getName()));
				colorStyle = getColor(workbook, c.getEnseignant().getName());
				colorStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				colorStyle.setFont(font);
				cell.setCellStyle(colorStyle);
			}
			cell = row.createCell(cellIndex++);
			if(c.getCandide() != null){
				cell.setCellValue(AlgoPlanningUtils.emailToName(c.getCandide().getName()));
				colorStyle = getColor(workbook, c.getCandide().getName());
				colorStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				colorStyle.setFont(font);
				cell.setCellStyle(colorStyle);
			}
			cell = row.createCell(cellIndex++);
			cell.setCellStyle(defaultStyle);
			cell.setCellValue(c.getTuteur().getName());
		}

		// Rearrange columns size
		for(int i=0; i < planningSheet.getPhysicalNumberOfRows(); i++) {
			row = planningSheet.getRow(i);
			row.setRowStyle(defaultStyle);
			Iterator<Cell> cellIterator = row.cellIterator();
			while (cellIterator.hasNext()) {
				cell = (HSSFCell) cellIterator.next();
				planningSheet.autoSizeColumn(cell.getColumnIndex());
			}
		}

		HSSFPrintSetup ps = planningSheet.getPrintSetup();
		ps.setLandscape(true);
		ps.setPaperSize(PrintSetup.A4_PAPERSIZE);
		planningSheet.setAutobreaks(true);
		planningSheet.setFitToPage(true);
		ps.setFitWidth((short)1);
		ps.setFitHeight((short)0);

		Footer footer = planningSheet.getFooter();
		footer.setLeft(planning.getName());
		footer.setRight( "Page " + HeaderFooter.page() + " sur " + HeaderFooter.numPages() );
		footer.setCenter(planning.getCsv_file());

//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Date now = new Date();
		String csvName = planning.getCsv_file();
		String[] split = planning.getCsv_file().split("\\.csv");
		if(split.length > 0) { csvName = split[0]; }
		File f = new File(planning.getName()+"_"+csvName+"_"+sdf.format(now)+".xls");
		FileOutputStream stream = new FileOutputStream(f);
		workbook.write(stream);
		stream.close();
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

	public HSSFCellStyle getColor( HSSFWorkbook workbook, String email){
		String color="";
		if(!couleurParProf.containsKey(email)){
			Short shortColor = defcouleur.get((couleurParProf.size())%defcouleur.size());

			HSSFCellStyle style = workbook.createCellStyle();
			style.setWrapText(true);
			style.setFillForegroundColor(shortColor);
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

			couleurParProf.put(email, style);
		}
		return couleurParProf.get(email);
	}
}
