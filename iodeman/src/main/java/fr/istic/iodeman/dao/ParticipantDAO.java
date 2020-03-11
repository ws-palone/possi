package fr.istic.iodeman.dao;

import fr.istic.iodeman.model.Participant;

import java.util.List;

public interface ParticipantDAO {

	public List<Participant> findAll();

	public void persist(Participant par);
	
	public Participant findById(int ID);

	public void delete(Participant par);

	public void deleteAll();
}
