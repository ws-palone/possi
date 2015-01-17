package fr.istic.iodeman.strategy;

import java.io.File;
import java.util.Collection;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.joda.time.DateTime;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Lists;

import fr.istic.iodeman.model.OralDefense;
import fr.istic.iodeman.model.Room;
import fr.istic.iodeman.model.TimeBox;
import fr.istic.iodeman.utils.AlgoPlanningUtils;

public class PlanningExcelExport implements PlanningExport {

	/**
	 * 
	 */
	Collection<OralDefense> oralDefenses;

	/**
	 * Collection of timeboxes where there will have a oral defense
	 */
	private Collection<TimeBox> timeboxes;

	/**
	 * the excel sheet
	 */
	WritableSheet sheet;

	/**
	 * 
	 */
	Collection<DateTime> singleDates;

	/**
	 * 
	 */
	Collection<Room> rooms;

	/**
	 * 
	 */
	int numberOfpersons = 4;

	/**
	 * Column index
	 */
	int nbHeaderLines = 0;

	/**
	 * Line index
	 */
	int nbHeaderColumns = 0;

	@Override
	public void configure(Collection<TimeBox> timeboxes) {
		this.timeboxes = timeboxes;
	}

	@Override
	public void execute(Collection<OralDefense> oralDefensesRaw) throws Exception {	
		// Ordering the oral defenses by starting date
		oralDefenses = AlgoPlanningUtils.sortOralDefensesByStartingDate(oralDefensesRaw);
		// Retrieve the single dates of the set of oral defenses, in order to write the hearder of the sheet
		singleDates = getSingleDatesFromOralDefenses(oralDefenses);
		// Retrieve the rooms
		rooms = getSingleRoomsFromOralDefense(oralDefenses);

		/**
		 * BEGIN generation of excel sheet
		 */
		// creation of the workbook
		WritableWorkbook workbook = Workbook.createWorkbook(new File("/tmp/planning.xls"));
		// creation of the sheet
		sheet = workbook.createSheet("Planning", 0);

		buildColumnHeader();
		buildHeaderLines();
		fillThePlanning();

		// write in the file then close it
		workbook.write();
		workbook.close();
		/**
		 * END generation of excel sheet
		 */

	}

	private Collection<DateTime> getSingleDatesFromOralDefenses(Collection<OralDefense> oralDefenses){
		Function<OralDefense, DateTime> getDate = new Function<OralDefense, DateTime>() {
			@Override
			public DateTime apply(OralDefense o) {
				return (new DateTime(o.getTimebox().getFrom())).withTimeAtStartOfDay();
			}
		};

		Collection<DateTime> singleDates = Lists.transform(Lists.newArrayList(oralDefenses), getDate);

		return Lists.newArrayList(ImmutableSortedSet.copyOf(singleDates));
	}

	private Collection<TimeBox> getUniqueTimeBoxByHour(Collection<TimeBox> timeboxes){

		DateTime lastDate = null;
		boolean firstPass = true;

		Collection<TimeBox> results = Lists.newArrayList();

		for(TimeBox t : timeboxes){
			DateTime datetime = new DateTime(t.getFrom());

			// first pass 
			if (firstPass) {
				lastDate = datetime;
			}

			if(!(lastDate.withTimeAtStartOfDay()).isEqual(datetime.withTimeAtStartOfDay())){
				break;
			}

			results.add(t);

			// not anymore the first pass
			firstPass = false;
		}

		return results;
	}

	private Collection<Room> getSingleRoomsFromOralDefense(Collection<OralDefense> oralDefenses){
		Function<OralDefense, Room> getRoom = new Function<OralDefense, Room>() {

			@Override
			public Room apply(OralDefense o) {
				return o.getRoom();
			}

		};

		Collection<Room> singleRooms = Lists.transform(Lists.newArrayList(oralDefenses), getRoom);

		return Lists.newArrayList(ImmutableSortedSet.copyOf(singleRooms));
	}

	private void buildColumnHeader() throws Exception{

		for(DateTime day : singleDates){
			// index  of column
			int i = (1+(rooms.size()*nbHeaderColumns));
			// index of line
			int j = 1;

			// merging of the cell of date
			sheet.mergeCells(i, j, (i+rooms.size()-1), j);

			// adding of text
			sheet.addCell(new Label(i, j, day.toString("E dd MM yyyy")));

			/**
			 * BEGIN Rooms
			 */
			j++;
			for(Room room : rooms){
				sheet.addCell(new Label(i, j, room.getName()));
				i++;
			}
			j--;
			/**
			 * END Rooms
			 */


			/**
			 * Style
			 */
			//			CellView cellview = sheet.getColumnView(i);
			//			cellview.setSize(5000000);
			//			sheet.setColumnView(i, cellview);
			nbHeaderColumns++;
		}
	}


	private void buildHeaderLines() throws Exception{

		Collection<TimeBox> uniqueTimeBoxes = getUniqueTimeBoxByHour(timeboxes);

		for(TimeBox t : uniqueTimeBoxes){
			// init of indexes
			int i = 0;
			int j = (3+(numberOfpersons*nbHeaderLines));

			// merge the cells according the number of persons who can assist to an oral defense
			sheet.mergeCells(i, j, i, (j+numberOfpersons-1));

			// value of the cell; HH:MM
			DateTime from = new DateTime(t.getFrom());
			DateTime to = new DateTime(t.getTo());
			String format = "HH:mm";
			sheet.addCell(new Label(i,j, from.toString(format)+ " / " + to.toString(format)));

			j+=numberOfpersons;
			nbHeaderLines++;
		}

	}

	private void fillThePlanning() throws Exception{			

		int line_top = 3;
		int currentDay = 0;
		int currentTimeBox = 0; 
		int lineIndex = line_top;

		DateTime lastTimebox = null;
		Boolean firstPass = true;

		for(TimeBox timebox : timeboxes){
			// current timebox date
			DateTime timeboxDate = new DateTime(timebox.getFrom());

			// back up of the first timebox in order to know when the day changes
			if (firstPass) lastTimebox = timeboxDate;


			// to see if we have changed of timebox
			if (!timeboxDate.isEqual(lastTimebox)) {
				currentTimeBox++;
//				lineIndex = (line_top + (currentTimeBox * numberOfpersons));
			}
			
			// if the day changed? Changement of the column index 
			if (!(timeboxDate.withTimeAtStartOfDay()).isEqual(lastTimebox.withTimeAtStartOfDay())){
				currentDay++;
				currentTimeBox = 0;
			}

			for(OralDefense o : Lists.newArrayList(oralDefenses)){
				// oral defense date
				DateTime odDate = new DateTime(o.getTimebox().getFrom());

				// it matches
				if (odDate.isEqual(timeboxDate)) {
					// column index for the room
					int indexRoom = (1+(currentDay*rooms.size()));
					
					// awarding of the right room
					for(Room room : rooms){
						// it matches
						
						if (o.getRoom().getName().equals(room.getName())) { // TODO compare ID!!
							lineIndex = (line_top + (currentTimeBox * numberOfpersons));
							sheet.addCell(new Label(indexRoom, lineIndex, o.getComposition().getStudent().getFirstName()));
							lineIndex++;	
							sheet.addCell(new Label(indexRoom, lineIndex, o.getComposition().getFollowingTeacher().getFirstName()));
							
							// we remove the oral defense
							oralDefenses.remove(o);
						}

						indexRoom++;
					}
				}

			}
			lastTimebox = new DateTime(timebox.getFrom()); 
			// at the end of the first pass
			firstPass = false;
		}

	}
}
