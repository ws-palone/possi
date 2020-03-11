package fr.istic.iodeman.dao;

import fr.istic.iodeman.model.OralDefense;

import java.util.List;

public interface OralDefenseDAO {

	public List<OralDefense> findAll();

	public void persist(OralDefense oral);

	public OralDefense findById(int ID);

	public void delete(OralDefense oral);

	public void deleteAll();
}
