package fr.istic.iodeman.dao;

import fr.istic.iodeman.model.Person;

import java.util.List;

public interface PersonDAO {

	public void persist(Person person);
	
	public Person findByUid(String uid);
	
	public Person findByNames(String names);
	
	public Person findByEmail(String email);

	public void delete(Person person);

	public List<Person> findAll();

	public void deleteAll();
}