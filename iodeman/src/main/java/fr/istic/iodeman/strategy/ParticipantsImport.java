package fr.istic.iodeman.strategy;

import java.io.File;
import java.util.Collection;

import fr.istic.iodeman.model.Participant;
import fr.istic.iodeman.resolver.PersonResolver;


public interface ParticipantsImport {
	
	public void configure(PersonResolver resolver);
	
	public Collection<Participant> execute(File file) throws Exception ;

}
