package fr.istic.iodeman.strategy;

import java.io.File;
import java.util.Collection;

import fr.istic.iodeman.model.Participant;


public interface ParticipantsImport {
	public Collection<Participant> execute(File file) throws Exception ;
}
