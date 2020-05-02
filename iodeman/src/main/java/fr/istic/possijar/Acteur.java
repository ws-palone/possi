package fr.istic.possijar;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 */

/**
 * @author François Esnault, Petit Emmanuel [M2 MIAGE]
 * @date 9 janv. 2016
 */
public abstract class Acteur implements Serializable {
	
	Map<Integer, Boolean> disponibilites;
	int nbSoutenances;
	List<Acteur> relations;
	String name;
	
	public Acteur(String name) {
		this.name = name;
		nbSoutenances = 0;
		relations = new ArrayList<>();
	}
	public Acteur(){
		nbSoutenances = 0;
		relations = new ArrayList<>();
	}

	public Map<Integer, Boolean> getDisponibilites() {
		return disponibilites;
	}

	public int getNbSoutenances() {
		return nbSoutenances;
	}
	
	public void incNbSoutenances() {
		this.nbSoutenances += 1;
	}

	public List<Acteur> getRelations() {
		return relations;
	}
	
	public void addRelation(Acteur a) {
		this.relations.add(a);
	}
	
	public void removeRelation(Acteur a) {
		this.relations.remove(a);
	}

	public String getName() {
		return name;
	}

	public int getDisponibilitesSoutenances() {
		return getPeriodesLibres()-getNbSoutenances();
	}
	
	protected int getPeriodesLibres() {
		int nbPeriodeLibre = 0;
		Set<Integer> keys = getDisponibilites().keySet();
		for(int i : keys) {
			if(getDisponibilites().get(i)) {
				nbPeriodeLibre++;
			}
		}
		return nbPeriodeLibre;
	}
	
	public void addDisponibilite(int periode) {
		disponibilites.put(periode, false);
		decNbSoutenance();
	}
	
	public void addIndisponibilite(int periode) {
		disponibilites.put(periode, false);
	}
	
	public void decNbSoutenance() {
		nbSoutenances--;
		//System.err.println(this + " DEC " + nbSoutenances);
	}
	
	public boolean nestPlusActeur() {
		return nbSoutenances==0;
	}
	
	public boolean aFaitToutesLesSoutenances() {
		return nbSoutenances==0;
	}
	
	public void setDefaultDisponibilites(int nbPeriodesEnTout) {
		disponibilites = new HashMap<>();
		for(int i = 0 ; i < nbPeriodesEnTout; i++) {
			disponibilites.put(i, true);
		}
	}

	public void resetDisponibilites(int nbPeriodesEnTout, Set<Integer> periodes) {
		setDefaultDisponibilites(nbPeriodesEnTout);
		for (Integer i : periodes) {
			addIndisponibilite(i);
		}
	}
	
	@Override
	public String toString() {
		return getName();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
	    if (obj == this) return true;
	    if (!(obj instanceof Acteur)) return false;
	    Acteur o = (Acteur) obj;
	    return o.name == this.name;
	}
	
}
