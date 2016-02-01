package fr.istic.iodeman.strategy_new;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 
 */

/**
 * @author François Esnault, Petit Emmanuel [M2 MIAGE]
 * @date 9 janv. 2016
 */
public class ListActeur {
	
	List<Acteur> list;
	
	public ListActeur() {
		list = new ArrayList<Acteur>();
	}
	
	public ListActeur(List<Acteur> l) {
		this.list = new ArrayList<Acteur>(l);
	}

	public Acteur get(String name) {
		for(Acteur a : list) {
			if(a.getName().equals(name)) {
				return a;
			}
		}
		return null;
	}

	public Acteur getActeurLeMoinsDisponible() {
		Acteur leMoinsDispo = null;
		for(Acteur a : list) {
			//System.err.println(a + " " + a.nestPlusActeur() + " " + a.getNbSoutenances());
			if(leMoinsDispo == null || (a.getDisponibilitesSoutenances() < leMoinsDispo.getDisponibilitesSoutenances())) {
				if(!a.nestPlusActeur()) {
					leMoinsDispo = a;
				}
			}
		}
		return leMoinsDispo;
	}

	public String toString() {
		StringBuilder n = new StringBuilder("[");
		for(Acteur a : list) {
			n.append(a + " ");
		}
		n.append("]");
		return n.toString();
	}
}
