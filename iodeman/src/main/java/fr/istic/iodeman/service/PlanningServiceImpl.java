package fr.istic.iodeman.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.istic.iodeman.dao.PlanningDAO;
import fr.istic.iodeman.model.Planning;

@Service
public class PlanningServiceImpl implements PlanningService {

	@Autowired
	private PlanningDAO planningDAO;
	
	public List<Planning> findAll() {
		
		return planningDAO.findAll();
	}

	public Planning findById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

}
