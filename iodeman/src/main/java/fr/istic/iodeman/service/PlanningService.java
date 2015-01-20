package fr.istic.iodeman.service;

import java.util.List;

import fr.istic.iodeman.model.Planning;

public interface PlanningService {

	public List<Planning> findAll();
	
	public Planning findById(Integer id);
	
}
