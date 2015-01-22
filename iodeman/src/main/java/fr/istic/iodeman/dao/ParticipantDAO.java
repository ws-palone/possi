package fr.istic.iodeman.dao;

import java.util.List;

import fr.istic.iodeman.model.Participant;

public interface ParticipantDAO {

	public List<Participant> findAll();

	public void persist(Participant par);
	
	public Participant findById(int ID);

	public void delete(Participant par);

	public void deleteAll();
}
