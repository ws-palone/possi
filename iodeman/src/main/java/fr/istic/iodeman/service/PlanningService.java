package fr.istic.iodeman.service;

import java.io.File;
import java.util.List;

import fr.istic.iodeman.model.Planning;

public interface PlanningService {

	public List<Planning> findAll();
	
	public Planning findById(Integer id);
	
	public Planning importPartcipants(Planning planning, File file) throws Exception;
	
}
