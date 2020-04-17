package fr.istic.iodeman.services;

import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.Unavailability;

public interface MailService {
	
	public String sendToEveryParticipant(Planning planning);
	
	public void notifyNewUnavailability(Unavailability unavailability);
	
	public void notifyUnavailabityRemoved(Unavailability unavailability);

}
