package fr.istic.iodeman.strategy;

import java.io.File;
import java.util.Collection;

import fr.istic.iodeman.dto.ExtractParticipantErrorDTO;
import fr.istic.iodeman.models.OralDefense;
import fr.istic.iodeman.resolver.PersonResolver;


public interface ParticipantsImport {
	
	public void configure(PersonResolver resolver);
	
	public Collection<OralDefense> execute(File file) throws Exception ;

	public Collection<ExtractParticipantErrorDTO> getErrorsImport();

}
