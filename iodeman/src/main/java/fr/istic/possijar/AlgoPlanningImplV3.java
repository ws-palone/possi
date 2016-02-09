/**
 * 
 */
package fr.istic.possijar;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Stream;

import javafx.collections.ObservableList;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import fr.istic.iodeman.model.Participant;
import fr.istic.iodeman.model.Planning;
import fr.istic.iodeman.model.Room;
import fr.istic.iodeman.model.TimeBox;
import fr.istic.iodeman.model.Unavailability;

/**
 * @author François Esnault, Petit Emmanuel [M2 MIAGE]
 * @date 1 févr. 2016
 */
public class AlgoPlanningImplV3 {

	/* VARIABLES */
	private int nbJours;
	private int nbPeriodesParJour;
	private int nbPeriodesEnTout;
	private int nbSalles;
	private int nbSoutenances;
	
	/* ALGORITHME */
	private int nbInserted = 0;
	private int log = 0;
	private String contrainteForte = "Tuteurs";

	/* DONNEES */
	private ListActeur enseignants;
	private ListActeur tuteurs;
	private List<Student> etudiants;
	private Map<Integer, List<Creneau>> planning;
	private List<Creneau> impossibleAInserer;
	private List<String> sallesSelectionnees;
	
	public AlgoPlanningImplV3() {
		enseignants = new ListActeur();
		tuteurs = new ListActeur();
		etudiants = new ArrayList<Student>();
		planning = new HashMap<Integer, List<Creneau>>();
		impossibleAInserer = new ArrayList<Creneau>();
		sallesSelectionnees = new ArrayList<String>();
	}
	

	public void configure(Planning planning_infos,
			Collection<Participant> participants,
			Collection<TimeBox> timeboxes,
			Collection<Unavailability> unavailabilities) {
		
		nbPeriodesEnTout = timeboxes.size();
		
		
		configureSalles(planning_infos.getRooms());
		
		configureCreneaux();
		
		configureParticipants(participants);
		
		configureUnavailabilities(timeboxes, unavailabilities);
		
		nbSoutenances = participants.size();
		
	}

	/**
	 * @param rooms
	 */
	private void configureSalles(Collection<Room> rooms) {
		nbSalles = rooms.size();
		for(Room r : rooms) {
			sallesSelectionnees.add(r.getName());
		}
	}


	private void configureUnavailabilities(Collection<TimeBox> timeboxes,
			Collection<Unavailability> unavailabilities) {
		getNbDePeriodesParJour(timeboxes);
		Map<String, Integer> resolveTimeBox = new HashMap<String, Integer>();
		int idTimeBox = 0;
		for(TimeBox t : timeboxes) {
			resolveTimeBox.put(t.getFrom().hashCode() + " " + t.getTo().hashCode(), idTimeBox++);
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
						s.getTuteur().addIndisponibilite(resolveTimeBox.get(u.getPeriod().getFrom().hashCode() + " " + u.getPeriod().getTo().hashCode()));
						break loop;
					}
				}
			}
		}
	}

	private void getNbDePeriodesParJour(Collection<TimeBox> timeboxes) {
		// TODO
		int i = 0;
		int d1 = -1;
		for(TimeBox t : timeboxes) {
			if(d1 == -1) {
				d1 = t.getFrom().getDate();
			}
			if(t.getFrom().getDate() == d1) {
				i++;
			}
		}
		System.err.println(i);
		nbPeriodesParJour = i;
	}


	private void configureCreneaux() {
		for(int periode = 0; periode < nbPeriodesEnTout ; periode++) {
			List<Creneau> salles = new ArrayList<Creneau>();
			planning.put(periode, salles);
		}
	}

	private void configureParticipants(Collection<Participant> participants) {
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
			
			addRelationBetweenTeacherAndTutor(teacher, tutor, e, t);
            
            etudiants.add(new Student(p.getStudent().getEmail(), e, t));
		}
	}
	
	public void configure(ObservableList<String> sallesSelectionnees,
			Calendar c, int nbPeriodesEnTout, int nbPeriodesParJour, String pathContraintesEns,
			String pathContraintesTut, String pathDonnees,
			String contrainteForte) {
		configureSalles(sallesSelectionnees);
		this.nbPeriodesEnTout = nbPeriodesEnTout;
		this.nbPeriodesParJour = nbPeriodesParJour;
		this.contrainteForte = contrainteForte;
		
		enseignants = new ListActeur();
		tuteurs = new ListActeur();
		etudiants = new ArrayList<Student>();
		planning = new HashMap<Integer, List<Creneau>>();
		impossibleAInserer = new ArrayList<Creneau>();
		this.sallesSelectionnees = new ArrayList<String>();
		
		configureCreneaux();
		CSVParser parser = new CSVParser();
		parser.readDispo(pathContraintesEns, Role.Enseignant, enseignants, nbPeriodesParJour);
		parser.readDispo(pathContraintesTut, Role.Tuteur, tuteurs, nbPeriodesParJour);
		nbSoutenances = parser.readCSV(pathDonnees, enseignants, tuteurs, etudiants, nbPeriodesEnTout);
		
	}
	
	private void configureSalles(ObservableList<String> rooms) {
		nbSalles = rooms.size();
		System.err.println(rooms);
		sallesSelectionnees.clear();
		for(String r : rooms) {
			sallesSelectionnees.add(r);
		}
	}

	private void addRelationBetweenTeacherAndTutor(String teacher,
			String tutor, Enseignant e, Tuteur t) {
		e.incNbSoutenances();
		e.incNbSoutenancesCandide();
		e.addRelation(tuteurs.get(tutor));
		
		t.incNbSoutenances();
		t.addRelation(enseignants.get(teacher));
	}
	
	public void execute() {
		for(int i = 0; i < nbSoutenances; i++) {
			insertCreneau();
		}
	}
	
	public void insertCreneau() {
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
			System.err.println(l.list);
			if(l.list.isEmpty()) {
				insertInComplementaryList(act, tmp, e, t, l);
				inserted = true;
				break insertion;
			}
			System.err.println("Acteur - dispo " + l.list);
			act = l.getActeurLeMoinsDisponible();
			Creneau c = getCreneauBetweenTeacherAndTutor(act, l, e, t);
			if(log==0) {
				System.err.println(c);
			}
			if(c==null) {
				System.err.println("On remove " + act);
				l.list.remove(act);
			} else {
				System.err.println("On insère dans la liste");
				inserted = true;
				insertInRealList(c);
				nbInserted++;
			}
		}
	}
	
	private Creneau getCreneauBetweenTeacherAndTutor(Acteur act, ListActeur l, Enseignant e, Tuteur t) {
		if(log==0) {
			System.err.println("On teste avec " + act);
		}
		if(act instanceof Enseignant) {
			e = (Enseignant)act;
		} else {
			t = (Tuteur)act;
		}
		if(log==0) {
			System.err.println("Enseignant " + e);
			System.err.println("Tuteur " + t);
		}
		Student s = getStudent(e, t);
		return creneauCommun(e, t, s);
	}

	private void insertInRealList(Creneau c) {
		if(log==0 || log==1) {
			System.err.println(nbInserted + "\n-----------------");
			System.err.println("\tEtudiant " + c.getStudent());
			System.err.println("\tEnseignant " + c.getEnseignant());
			System.err.println("\tTuteur " + c.getTuteur());
			System.err.println("\tCandide " + c.getCandide());
			System.err.println("\tA la période " + c.getPeriode());
		}
		
		Enseignant e = c.getEnseignant();
		Tuteur t = c.getTuteur();
		Student s = c.getStudent();
		
		e.addDisponibilite(c.getPeriode());
		t.addDisponibilite(c.getPeriode());
		c.getCandide().addDisponibiliteCandide(c.getPeriode());
		
		removeRelation(e, t, s);
		
		testerSiActeurARealiserToutesSesSoutenances(e, t);
		if(c.getCandide().aFaitToutesLesSoutenances()) {
			enseignants.list.remove(c.getCandide());
		}
		
		insertCreneauInPlanning(c);
	}
	
	private void insertInComplementaryList(Acteur act, Acteur tmp, Enseignant e, Tuteur t, ListActeur l) {
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
		
		Student s = getStudent(e, t);
		removeRelation(e, t, s);
		
		testerSiActeurARealiserToutesSesSoutenances(e, t);
		
		impossibleAInserer.add(new Creneau(-1, e, null, t, s));
	}
	
	private void testerSiActeurARealiserToutesSesSoutenances(Enseignant e,
			Tuteur t) {
		if(e.aFaitToutesLesSoutenances()) {
			enseignants.list.remove(e);
		}
		if(t.aFaitToutesLesSoutenances()) {
			tuteurs.list.remove(t);
		}
	}

	private void removeRelation(Enseignant e, Tuteur t, Student s) {
		removeStudent(s);
		e.removeRelation(t);
		t.removeRelation(e);
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
				//System.err.println(s.getEnseignant() + " " + e + " ET " + s.getTuteur() + " " + t);
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
		getDispoCandide(listeCandide, dispoCandide);
		
		Enseignant c = null;

		if(log==0) {
			System.err.println(listeCandide);
		}
		
		Map<Integer, Integer> creneauxPonderations = new HashMap<Integer, Integer>();

		Set<Integer> dispoEns = dispoEnseignant.keySet();
		initializeDispoEnseignants(dispoEnseignant, dispoTuteur,
				creneauxPonderations, dispoEns);
		
		Set<Integer> periodes = creneauxPonderations.keySet();
		pondereSelonPriorites(t, creneauxPonderations, periodes);
		
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

	private void pondereSelonPriorites(Tuteur t,
			Map<Integer, Integer> creneauxPonderations, Set<Integer> periodes) {
		for(int periode : periodes) {
			int value = creneauxPonderations.get(periode);
			
			int contrTuteurs = -5;
			int contrHoraires = 0;
			
			if(contrainteForte.equals("Tuteurs")) {
				//contrTuteurs = -20;
			} else {
				//contrHoraires = -15;
			}
			
			int periodeMiddle1, periodeMiddle2;
			periodeMiddle1 = (int)Math.floor(nbPeriodesParJour/2);
			periodeMiddle2 = (int)Math.ceil(nbPeriodesParJour/2);
			int res = periode%nbPeriodesParJour;
			while (periodeMiddle1 >= 0 || periodeMiddle2 <= nbPeriodesParJour) {
				if(res==periodeMiddle1 || res==periodeMiddle2) {
					creneauxPonderations.put(periode, value+contrHoraires);
				} 
				contrHoraires+=2;
				periodeMiddle1--;
				periodeMiddle2++;
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
			
			//System.err.println("Période " + periode + " + " + creneauxPonderations.get(periode));
		}
	}

	private void initializeDispoEnseignants(
			Map<Integer, Boolean> dispoEnseignant,
			Map<Integer, Boolean> dispoTuteur,
			Map<Integer, Integer> creneauxPonderations, Set<Integer> dispoEns) {
		for(int p : dispoEns) {
			if(dispoEnseignant.get(p) && dispoTuteur.get(p)) {
				creneauxPonderations.put(p, 10);
			}
		}
	}

	private void getDispoCandide(List<Acteur> listeCandide,
			Map<Acteur, Integer> dispoCandide) {
		for(Acteur candide : listeCandide) {
			Enseignant ens = (Enseignant)candide;
			//System.err.println(ens);
			if(ens.getNbSoutenancesCandide()>0) {
				System.err.println(ens + " " + ens.getNbSoutenancesCandide());
				dispoCandide.put(ens, ens.getNbSoutenancesCandide());
			}
		}
	}
	
	public static <K, V extends Comparable<? super V>> Map<K, V> 
    sortByValue( Map<K, V> map ) {
    List<Map.Entry<K, V>> list =
        new LinkedList<>( map.entrySet() );
    Collections.sort( list, new Comparator<Map.Entry<K, V>>()
    {
        @Override
        public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
        {
        	System.err.println(o1.getValue() + " compare to " + o2.getValue() + " ---> " + (o1.getValue()).compareTo( o2.getValue() ));
            int res = (o1.getValue()).compareTo( o2.getValue() );
            if(res==0) {
            	return o1.getKey().toString().compareTo(o2.getKey().toString());
            }
        	return res;
        }
    } );

    Map<K, V> result = new LinkedHashMap<>();
    for (Map.Entry<K, V> entry : list)
    {
        result.put( entry.getKey(), entry.getValue() );
    }
    return result;
}
	


	public File getFile() throws IOException {
		StringBuilder sb = new StringBuilder();
		Set<Integer> periodes = planning.keySet();
		sb.append("sep=,\n");
		for(int periode : periodes) {
			if(periode%nbPeriodesParJour==0) {
				sb.append(",,,,,,,,,,,\n,");
				for(String salle : sallesSelectionnees) {
					sb.append(salle + ",,,,,");
				}
				sb.append("\n");
			}
			List<Creneau> creneaux = planning.get(periode);
			for(Creneau c : creneaux) {
					if(c.getSalle()==1) {
						sb.append("P" + periode%nbPeriodesParJour + ",");
					}
					sb.append(c.getStudent() + ","
							+ c.getTuteur() + ","
							+ c.getEnseignant() + ","
							+ c.getCandide() + ", ,");
			}
			if(creneaux.size()==1) {
				sb.append(",,,,,");
			}
			sb.append("\n");
		}
		
		sb.append(",,,,,,,,,,,\n");
		sb.append(",,,,,,,,,,,\n");
		sb.append("Soutenances qui posent problemes :,,,,,,,,,,,\n");
		for(Creneau c : impossibleAInserer) {
			sb.append(c.getStudent() + ","
					+ c.getTuteur() + ","
					+ c.getEnseignant() + "\n");
		}
		
		System.err.println(sb.toString());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date now = new Date();
		File f = new File("Soutenances_"+sdf.format(now)+".csv");
		Files.write(sb.toString(), f, Charsets.UTF_8);
		return f;
	}
}
