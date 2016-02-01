/**
 * 
 */
package fr.istic.iodeman.strategy_new;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Stream;

import fr.istic.iodeman.model.OralDefense;
import fr.istic.iodeman.model.Participant;
import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.TimeBox;
import fr.istic.iodeman.model.Unavailability;
import fr.istic.iodeman.strategy.AlgoPlanningV2;
import fr.istic.iodeman.strategy.PlanningDataValidator;
import fr.istic.iodeman.strategy.PlanningDataValidatorImpl;

/**
 * @author François Esnault, Petit Emmanuel [M2 MIAGE]
 * @date 1 févr. 2016
 */
public class AlgoPlanningImplV3 implements AlgoPlanningV2 {

	/* VARIABLES */
	private int nbJours;
	private int nbPeriodesParJour;
	private int nbPeriodesEnTout;
	private int nbSalles;
	
	/* ALGORITHME */
	private boolean isFinised = false;
	private int nbInserted = 0;
	private int log = 1;
	boolean fastInsert = false;
	String contrainteForte = "Tuteurs";

	/* DONNEES */
	ListActeur enseignants;
	ListActeur tuteurs;
	List<Student> etudiants;
	Map<Integer, List<Creneau>> planning;
	List<Creneau> impossibleAInserer;

	public void configure(Planning planning_infos,
			Collection<Participant> participants,
			Collection<TimeBox> timeboxes,
			Collection<Unavailability> unavailabilities) {
		
		enseignants = new ListActeur();
		tuteurs = new ListActeur();
		etudiants = new ArrayList<Student>();
		planning = new HashMap<Integer, List<Creneau>>();
		impossibleAInserer = new ArrayList<Creneau>();
		
		nbPeriodesEnTout = timeboxes.size();
		nbSalles = planning_infos.getRooms().size();
		
		PlanningDataValidator validator = new PlanningDataValidatorImpl();
		validator.configure(planning_infos, participants, timeboxes);
		validator.validate();
		
		for(int periode = 0; periode < nbPeriodesEnTout ; periode++) {
			List<Creneau> salles = new ArrayList<Creneau>();
			planning.put(periode, salles);
		}
		
		for(Participant p : participants) {
			String teacher = p.getFollowingTeacher().getEmail();
			String tutor = p.getTutorFullName();
			
			Enseignant e = (Enseignant) enseignants.get(teacher);
			if(e==null) {
				e = new Enseignant(teacher);
				e.setDefaultDisponibilites(nbPeriodesEnTout); 
				enseignants.list.add(e);
			}
			
			Tuteur t = (Tuteur) tuteurs.get(tutor);
            if(t==null) {
            	t = new Tuteur(tutor);
            	t.setDefaultDisponibilites(nbPeriodesEnTout);
            	tuteurs.list.add(t);
            }
			
			e.incNbSoutenances();
            e.incNbSoutenancesCandide();
            e.addRelation(tuteurs.get(tutor));
            
            t.incNbSoutenances();
            t.addRelation(enseignants.get(teacher));
            
            etudiants.add(new Student(p.getStudent().getEmail(), e, t));
		}
		
		Map<String, Integer> resolveTimeBox = new HashMap<String, Integer>();
		int idTimeBox = 0;
		for(TimeBox t : timeboxes) {
			resolveTimeBox.put(t.getFrom().hashCode() + " " + t.getTo().hashCode(), idTimeBox++);
			//System.err.println(t.getFrom().hashCode() + " " + t.getTo().hashCode());
		}
		
		for(Unavailability u : unavailabilities) {
			String email = u.getPerson().getEmail();
			if(enseignants.get(email)!=null) {
				enseignants.get(email).addDisponibilite(resolveTimeBox.get(u.getPeriod().getFrom() + " " + u.getPeriod().getTo()));
			} else {
				Iterator<Student> it = etudiants.iterator();
				loop:
				while(it.hasNext()) {
					Student s = it.next();
					if(s.getName().equals(email)) {
						System.err.println("Student " + s);
						System.err.println("Tuteur " + s.getTuteur());
						System.err.println(u.getPeriod().getFrom() + " " + u.getPeriod().getTo());
						System.err.println(resolveTimeBox.get(u.getPeriod().getFrom().hashCode() + " " + u.getPeriod().getTo().hashCode()));
						s.getTuteur().addIndisponibilite(resolveTimeBox.get(u.getPeriod().getFrom().hashCode() + " " + u.getPeriod().getTo().hashCode()));
						break loop;
					}
				}
			}
		}
		
		for(int i = 0; i < participants.size(); i++) {
			insertData();
		}
		
	}
	
	public void insertData() {
		boolean inserted = false;
		
		
		Acteur act = getActeurLeMoinsDisponible();
		if(log==0) {
			System.err.println("On récupère l'acteur le moins disponible (enseignant ou tuteur)" + act);
		}
		Acteur tmp = act;
		Enseignant e = null;
		Tuteur t = null;
		if(act instanceof Enseignant) {
			e = (Enseignant)act;
		} else {
			t = (Tuteur)act;
		}
		if(log==0) {
			System.err.println("");
			System.err.println("On récupère les acteurs en relations les moins disponibles");
		}
		
		ListActeur l = new ListActeur(act.getRelations());
		if(log==0) {
			System.err.println("Liste des acteurs en relation avec " + act + " => " + l);
		}
		
		insertion:
		while(!inserted) {
			if(l.list.isEmpty()) {
				if(log==0) {
					System.err.println("Impossible d'insérer l'acteur. On le place dans une liste complémentaire");
				}
				if(tmp instanceof Enseignant) {
					e = (Enseignant)tmp;
				} else {
					t = (Tuteur)tmp;
				}
				l = new ListActeur(tmp.getRelations());
				if(log==0) {
					System.err.println("Liste " + l);
				}
				act = l.getActeurLeMoinsDisponible();
				
				if(act instanceof Enseignant) {
					e = (Enseignant)act;
				} else {
					t = (Tuteur)act;
				}
				
				if(log==0) {
					System.err.println("Enseignant à supprimer : " + e);
					System.err.println("Tuteur à supprimer : " + t);
				}
				
				e.decNbSoutenance();
				t.decNbSoutenance();
				
				e.removeRelation(t);
				t.removeRelation(e);
				
				if(e.aFaitToutesLesSoutenances()) {
					enseignants.list.remove(e);
				}
				if(t.aFaitToutesLesSoutenances()) {
					tuteurs.list.remove(t);
				}
				
				if(log==0) {
					System.err.println("SOUTENANCES DU TUTEURS " + t.getNbSoutenances());
				}
				
				Student s = getStudent(e, t);
				removeStudent(s);
				if(log==0) {
					System.err.println("Etudiant " + s);
				}
				impossibleAInserer.add(new Creneau(-1, e, null, t, s));
				
				inserted = true;
				break insertion;
			}
			
			act = l.getActeurLeMoinsDisponible();
			if(log==0) {
				System.err.println("On teste avec " + act);
			}
			if(act instanceof Enseignant) {
				e = (Enseignant)act;
			} else {
				t = (Tuteur)act;
			}
			if(log==0) {
				System.err.println("Acteur " + act);
				System.err.println("Tuteur " + t);
			}
			Student s = getStudent(e, t);
			Creneau c = creneauCommun(e, t, s);
			if(log==0) {
				System.err.println(c);
			}
			if(c==null) {
				l.list.remove(act);
			} else {
				inserted = true;
				
				if(log==0 || log==1) {
					System.err.println(nbInserted + "\n-----------------");
					System.err.println("\tEtudiant " + c.getStudent());
					System.err.println("\tEnseignant " + c.getEnseignant());
					System.err.println("\tTuteur " + c.getTuteur());
					System.err.println("\tCandide " + c.getCandide());
					System.err.println("\tA la période " + c.getPeriode());
				}
				
				e.addDisponibilite(c.getPeriode());
				t.addDisponibilite(c.getPeriode());
				c.getCandide().addDisponibiliteCandide(c.getPeriode());
				
				e.removeRelation(t);
				t.removeRelation(e);
				removeStudent(s);
				
				System.err.println("Enseignant " + e + " " + e.getNbSoutenances() + " " + e.getNbSoutenancesCandide());
				System.err.println("Tuteur " + t + " " + t.getNbSoutenances());
				System.err.println("Candide " + c.getCandide() + " " + c.getCandide().getNbSoutenances() + " " + c.getCandide().getNbSoutenancesCandide());
				
				if(e.aFaitToutesLesSoutenances()) {
					enseignants.list.remove(e);
				}
				if(t.aFaitToutesLesSoutenances()) {
					tuteurs.list.remove(t);
				}
				if(c.getCandide().aFaitToutesLesSoutenances()) {
					enseignants.list.remove(c.getCandide());
				}
				
				insertCreneauInPlanning(c);
				
				nbInserted++;
			}
		}
	}
	
	private void insertCreneauInPlanning(Creneau c) {
		List<Creneau> salles = planning.get(c.getPeriode());
		
		int size = salles.size();
		c.setSalle(size+1);
		salles.add(c);
	}

	private Student getStudent(Enseignant e, Tuteur t) {
		for(Student s : etudiants) {
			if(log==0) {
				System.err.println(s.getEnseignant() + " " + e + " ET " + s.getTuteur() + " " + t);
			}
			if(s.getEnseignant() == e && s.getTuteur() == t) {
				return s;
			}
		}
		return null;
	}
	
	private void removeStudent(Student s) {
		etudiants.remove(s);
	}

	public Acteur getActeurLeMoinsDisponible() {
		
		Acteur e = enseignants.getActeurLeMoinsDisponible();
		Acteur t = tuteurs.getActeurLeMoinsDisponible();
		
		if(e.getDisponibilitesSoutenances()<t.getDisponibilitesSoutenances()) {
			return e;
		} else {
			return t;
		}
	}
	
	public Creneau creneauCommun(Enseignant e, Tuteur t, Student s) {
		if(log==0) {
			System.err.println(e + " " + t);
		}
		Map<Integer, Boolean> dispoEnseignant = e.getDisponibilites();
		Map<Integer, Boolean> dispoTuteur = t.getDisponibilites();

		List<Acteur> listeCandide = new ArrayList<Acteur>(enseignants.list);
		listeCandide.remove(e);
		Map<Acteur, Integer> dispoCandide = new HashMap<Acteur, Integer>();
		for(Acteur candide : listeCandide) {
			Enseignant ens = (Enseignant)candide;
			if(ens.getNbSoutenancesCandide()>0) {
				dispoCandide.put(ens, ens.getNbSoutenancesCandide());
			}
		}
		
		Enseignant c = null;

		if(log==0) {
			System.err.println(listeCandide);
		}
		
		Map<Integer, Integer> creneauxPonderations = new HashMap<Integer, Integer>();

		Set<Integer> dispoEns = dispoEnseignant.keySet();
		for(int p : dispoEns) {
			if(dispoEnseignant.get(p) && dispoTuteur.get(p)) {
				creneauxPonderations.put(p, 10);
			}
		}
		
		Set<Integer> periodes = creneauxPonderations.keySet();
		for(int periode : periodes) {
			int value = creneauxPonderations.get(periode);
			
			int contrTuteurs = 5;
			int contrHoraires = 10;
			
			if(contrainteForte.equals("Tuteurs")) {
				contrTuteurs = -20;
			} else {
				contrHoraires = -15;
			}
			
			int res = periode%8;
			if(res==3 || res==4) {
				creneauxPonderations.put(periode, value+contrHoraires);
			} else if (res==2 || res==5) {
				creneauxPonderations.put(periode, value+contrHoraires+4);
			} else if (res==1 || res==6) {
				creneauxPonderations.put(periode, value+contrHoraires+6);
			} else if (res==0 || res==7) {
				creneauxPonderations.put(periode, value+contrHoraires+8);
			}
			
			value = creneauxPonderations.get(periode);
			
			if((periode%8)!=0) {
				List<Creneau> avant = planning.get(periode-1);
				for(Creneau creneau : avant) {
					if(creneau.getTuteur() == t) {
						creneauxPonderations.put(periode, value+contrTuteurs);
					}
				}
			} else if((periode%8)!=7) {
				List<Creneau> apres = planning.get(periode+1);
				for(Creneau creneau : apres) {
					if(creneau.getTuteur() == t) {
						creneauxPonderations.put(periode, value+contrTuteurs);
					}
				}
			}
		}
		
		if(log==0) {
			System.err.println("Les creneaux communs entre " + e + " et " + t + " sont " + creneauxPonderations.keySet());
		}
		
		creneauxPonderations = sortByValue(creneauxPonderations);
		dispoCandide = sortByValue(dispoCandide);

		if(log==0) {
			System.err.println("Les creneaux communs entre " + e + " et " + t + " sont " + creneauxPonderations.keySet());
		}
		
		for(int periode : creneauxPonderations.keySet()) {
			for(Acteur act: dispoCandide.keySet()) {
				if(log==0) {
					//System.err.println(act);
				}
				c = (Enseignant)act;
				if(c.getDisponibilites().get(periode)) {
					if(planning.get(periode).size()<nbSalles) {
						return new Creneau(periode, e, c, t, s);
					}
				}
			}
		}
			
		return null;
	}
	
	@Override
	public Collection<OralDefense> execute() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public int getNbJours() {
		return nbJours;
	}
	public void setNbJours(int nbJours) {
		this.nbJours = nbJours;
	}
	public int getNbPeriodesParJour() {
		return nbPeriodesParJour;
	}
	public void setNbPeriodesParJour(int nbPeriodesParJour) {
		this.nbPeriodesParJour = nbPeriodesParJour;
	}
	public int getNbPeriodesEnTout() {
		return nbPeriodesEnTout;
	}
	public void setNbPeriodesEnTout(int nbPeriodesEnTout) {
		this.nbPeriodesEnTout = nbPeriodesEnTout;
	}
	public int getNbSalles() {
		return nbSalles;
	}
	public void setNbSalles(int nbSalles) {
		this.nbSalles = nbSalles;
	}
	public boolean isFastInsert() {
		return fastInsert;
	}
	public void setFastInsert(boolean fastInsert) {
		this.fastInsert = fastInsert;
	}
	public String getContrainteForte() {
		return contrainteForte;
	}
	public void setContrainteForte(String contrainteForte) {
		this.contrainteForte = contrainteForte;
	}
	public ListActeur getEnseignants() {
		return enseignants;
	}
	public void setEnseignants(ListActeur enseignants) {
		this.enseignants = enseignants;
	}
	public ListActeur getTuteurs() {
		return tuteurs;
	}
	public void setTuteurs(ListActeur tuteurs) {
		this.tuteurs = tuteurs;
	}
	public List<Student> getEtudiants() {
		return etudiants;
	}
	public void setEtudiants(List<Student> etudiants) {
		this.etudiants = etudiants;
	}
	public Map<Integer, List<Creneau>> getPlanning() {
		return planning;
	}
	public void setPlanning(Map<Integer, List<Creneau>> planning) {
		this.planning = planning;
	}
	public List<Creneau> getImpossibleAInserer() {
		return impossibleAInserer;
	}
	public void setImpossibleAInserer(List<Creneau> impossibleAInserer) {
		this.impossibleAInserer = impossibleAInserer;
	}
	
	static <K, V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> map )
	{
	  Map<K,V> result = new LinkedHashMap<>();
	 Stream <Entry<K,V>> st = map.entrySet().stream();

	 st.sorted(Comparator.comparing(e -> e.getValue()))
	      .forEachOrdered(e ->result.put(e.getKey(),e.getValue()));

	 return result;
	}

}
