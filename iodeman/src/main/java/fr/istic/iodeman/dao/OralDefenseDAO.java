package fr.istic.iodeman.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import fr.istic.iodeman.model.OralDefense;
import fr.istic.iodeman.model.Person;

public interface OralDefenseDAO {

	public List<OralDefense> findAll();

	public void persist(OralDefense oral);

	public OralDefense findById(int ID);

	public void delete(OralDefense oral);

	public void deleteAll();
}
