package fr.istic.iodeman.dao;

import java.util.List;

import fr.istic.iodeman.model.TimeBox;

public interface TimeBoxDAO {

    public List<TimeBox> findAll();
	
	public void persist(TimeBox time);
	
	public TimeBox findById(Integer ID);
	
	public void delete(TimeBox time);
	
	public void deleteAll();
}
