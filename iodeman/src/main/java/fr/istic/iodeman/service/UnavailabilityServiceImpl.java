package fr.istic.iodeman.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.istic.iodeman.dao.UnavailabilityDAO;
import fr.istic.iodeman.model.Unavailability;

@Service
public class UnavailabilityServiceImpl implements UnavailabilityService{

	@Autowired
	private UnavailabilityDAO unavailabilityDAO;
	
	@Override
	public List<Unavailability> findById(Integer idPlanning, String uid) {
		
		return unavailabilityDAO.findById(idPlanning, uid);
	}
	
}
