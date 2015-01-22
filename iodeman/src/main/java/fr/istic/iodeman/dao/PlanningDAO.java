package fr.istic.iodeman.dao;

import java.util.List;

import fr.istic.iodeman.model.Planning;

public interface PlanningDAO {

	public List<Planning> findAll();
	
	public void persist(Planning pla);
	
	public Planning findById(Integer ID);
	
	public void delete(Planning pla);
	
	public void deleteAll();

}
