package fr.istic.iodeman.dao;

import java.util.List;

import fr.istic.iodeman.model.Unavailability;

public interface UnavailabilityDAO {
	public void persist(Unavailability u);
	
	public Unavailability findById(int id);

	public void delete(Unavailability u) ;

	public List<Unavailability> findAll();

	public void deleteAll();

}
