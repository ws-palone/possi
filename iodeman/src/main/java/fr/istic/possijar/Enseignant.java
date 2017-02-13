package fr.istic.possijar;

import java.io.Serializable;
/**
 * 
 */

/**
 * @author François Esnault, Petit Emmanuel [M2 MIAGE]
 * @date 9 janv. 2016
 */
public class Enseignant extends Acteur implements Serializable {
	
	int nbSoutenanceCandide;

	/**
	 * @param name
	 */
	public Enseignant(String name) {
		super(name);
		nbSoutenanceCandide = 0;
	}

	public Enseignant(){
		super();
	}

	
	public int getNbSoutenancesCandide() {
		return nbSoutenanceCandide;
	}
	
	public void incNbSoutenancesCandide() {
		this.nbSoutenanceCandide += 1;
	}

	public void setNbSoutenances(int nbSoutenanceCandide) {
		this.nbSoutenanceCandide = nbSoutenanceCandide;
	}
	
	public int getDisponibilitesSoutenances() {
		return getPeriodesLibres()-getNbSoutenances()-getNbSoutenancesCandide();
	}

	public void addDisponibiliteCandide(int periode) {
		disponibilites.put(periode, false);
		nbSoutenanceCandide--;
	}

	public boolean aFaitToutesLesSoutenances() {
		return nbSoutenanceCandide==0 && nbSoutenances==0;
	}


}
