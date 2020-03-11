package fr.istic.iodeman.dao;

import fr.istic.iodeman.model.Priority;

import java.util.List;

public interface PriorityDAO {

	public List<Priority> findAll();

	public void persist(Priority priority);
	
	public void update(Priority priority);

	public Priority findById(int ID);

	public void delete(Priority priority);

	public void deleteAll();

}
