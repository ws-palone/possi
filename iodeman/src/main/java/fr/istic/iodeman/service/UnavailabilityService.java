package fr.istic.iodeman.service;

import java.util.Date;
import java.util.List;

import fr.istic.iodeman.model.TimeBox;
import fr.istic.iodeman.model.Unavailability;

public interface UnavailabilityService {

	public List<Unavailability> findById(Integer id, String uid);

	public Unavailability create(Integer id, String uidperson, TimeBox period);

}
