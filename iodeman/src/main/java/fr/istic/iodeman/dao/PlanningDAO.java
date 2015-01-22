package fr.istic.iodeman.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import fr.istic.iodeman.model.Participant;
import fr.istic.iodeman.model.Planning;

public interface PlanningDAO {

	public List<Planning> findAll();
	
	public void persist(Planning pla);
	
	public Planning findById(Integer ID);
	
	public void delete(Planning pla);
	
	public void deleteAll();

}
