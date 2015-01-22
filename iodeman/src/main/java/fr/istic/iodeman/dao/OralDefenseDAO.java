package fr.istic.iodeman.dao;

import java.util.List;

import fr.istic.iodeman.model.OralDefense;

public interface OralDefenseDAO {

	public List<OralDefense> findAll();

	public void persist(OralDefense oral);

	public OralDefense findById(int ID);

	public void delete(OralDefense oral);

	public void deleteAll();
}
