package fr.istic.iodeman.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.Priority;

public interface PriorityDAO {

	public List<Priority> findAll();

	public void persist(Priority prio);

	public Priority findById(int ID);

	public void delete(Priority prio);

	public void deleteAll();

}
