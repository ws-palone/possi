package fr.istic.iodeman.service;

import fr.istic.iodeman.model.Planning;

public interface MailService {
	
	public String sendToEveryParticipant(Planning planning);

}
