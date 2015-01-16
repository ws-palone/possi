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
		
		buildHeaders();
		buildColumns();
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
	
	private void buildHeaders() throws Exception{
		// index  of column
		int i = 1;
		// index of line
		int j = 1;

		for(DateTime d : singleDates){
			// merging of the cell of date
			sheet.mergeCells(i, j, (i+rooms.size()-1), j);
			
			// adding of text
			sheet.addCell(new Label(i, j, d.toString("E dd MM yyyy")));
			
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
		}
	}
	
	private void buildColumns() throws Exception{
		// init of indexes
		int i = 0; int j = 3;
		for(TimeBox tb : timeboxes){
			// merge the cells according the number of persons who can assist to an oral defense
			sheet.mergeCells(i, j, i, (j+numberOfpersons-1));
			
			// value of the cell; HH:MM
			DateTime from = new DateTime(tb.getFrom());
			DateTime to = new DateTime(tb.getTo());
			String format = "HH:mm";
			sheet.addCell(new Label(i,j, from.toString(format)+ " / " + to.toString(format)));
			
			j+=numberOfpersons;
		}

	}

	private void fillThePlanning() throws Exception{	

	}
}
