package fr.istic.iodeman.factory;

import java.util.Collection;

import com.google.common.collect.Lists;

import fr.istic.iodeman.model.OralDefense;
import fr.istic.iodeman.model.Participant;
import fr.istic.iodeman.model.Person;
import fr.istic.iodeman.model.Room;
import fr.istic.iodeman.model.TimeBox;

public class OralDefenseFactory {

	public static OralDefense createOralDefense(Participant composition, Room room, TimeBox timebox) throws IllegalArgumentException {
		
		if (composition == null || room == null || timebox == null) {
			
			throw new IllegalArgumentException("Cannot create oral defense!");
			
		}
		
		Collection<Person> jury = Lists.newArrayList();
		
		OralDefense oralDefense = new OralDefense();
		oralDefense.setComposition(composition);
		oralDefense.setRoom(room);
		oralDefense.setTimebox(timebox);
		oralDefense.setJury(jury);
		
		return oralDefense;
		
	}
	
}
