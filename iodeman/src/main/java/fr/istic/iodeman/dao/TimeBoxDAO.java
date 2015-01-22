package fr.istic.iodeman.dao;

import java.util.List;

import fr.istic.iodeman.model.TimeBox;

public interface TimeBoxDAO {
	public void persist(TimeBox r);
	
	public TimeBox findById(int id);

	public void delete(TimeBox r) ;

	public List<TimeBox> findAll();

	public void deleteAll();
}
