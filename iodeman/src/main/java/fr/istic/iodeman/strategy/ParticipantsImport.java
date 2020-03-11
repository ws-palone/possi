package fr.istic.iodeman.strategy;

import fr.istic.iodeman.model.Participant;
import fr.istic.iodeman.resolver.PersonResolver;

import java.io.File;
import java.util.Collection;


public interface ParticipantsImport {
	
	public void configure(PersonResolver resolver);
	
	public Collection<Participant> execute(File file) throws Exception ;

}
