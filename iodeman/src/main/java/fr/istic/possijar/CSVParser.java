package fr.istic.possijar;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.collections.ObservableList;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class CSVParser {

    public void readDispo(String donnees, Role r, ListActeur acteurs, int periodesParJour) {

        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {

            br = new BufferedReader(new FileReader(donnees));
            while ((line = br.readLine()) != null) {
                // use comma as separator
            	line += " ";
                String[] row = line.split(cvsSplitBy);
                if(!row[0].equals("")) {
                    Map<Integer, Boolean> contraintes = new HashMap<>();
                    int periodeEnCours = 0;
                    for(int i = 1; i < row.length; i++) {
                        for(int j = 0; j < periodesParJour; j++) {

                            if(row[i].contains("X")) {
                                contraintes.put(periodeEnCours, true);
                            }
                            else if(row[i].contains("M")){
                                if(j <= periodesParJour/2 -1) {
                                    contraintes.put(periodeEnCours, true);
                                }
                                else {
                                    contraintes.put(periodeEnCours, false);
                                }
                            }
                            else if(row[i].contains("AM")){
                                if(j > periodesParJour/2 -1) {
                                    contraintes.put(periodeEnCours, true);
                                }
                                else {
                                    contraintes.put(periodeEnCours, false);
                                }
                            }
                            else {
                                contraintes.put(periodeEnCours, false);
                            }
                            periodeEnCours++;
                        }
                    }
                    
            		
                    Acteur a = null;
                    if(r == Role.Enseignant) {
                    	a = new Enseignant(row[0].trim());
                    }
                    if(r == Role.Tuteur) {
                    	a = new Tuteur(row[0].trim());
                    }
                    a.setDisponibilites(contraintes);
                    acteurs.list.add(a);
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

	/**
	 * @param donnees 
	 * @param enseignants
	 * @param tuteurs
	 * @param etudiants 
	 * @param nbSoutenancesEnseignants
	 * @param nbSoutenancesTuteurs
	 * @param relationsEnseignants
	 * @param relationsTuteurs
	 * @param N
	 */
	public int readCSV(String donnees, ListActeur enseignants, ListActeur tuteurs, List<Student> etudiants, int N) {

        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        int nbSoutenance = 0;
        
        try {

            br = new BufferedReader(new FileReader(donnees));
            br.readLine();
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] row = line.split(cvsSplitBy);
                String stu_name = row[0].trim();
                String ens_name = row[4].trim();
                String tut_name = row[5].trim();
                
                Enseignant e = (Enseignant)enseignants.get(ens_name);
                e.incNbSoutenances();
                e.incNbSoutenancesCandide();
                e.addRelation(tuteurs.get(tut_name));
                
                Tuteur t = (Tuteur)tuteurs.get(tut_name);
                t.incNbSoutenances();
                t.addRelation(enseignants.get(ens_name));
                
                etudiants.add(new Student(stu_name, e, t));
                
                nbSoutenance++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
		
        return nbSoutenance;
	}
	
	public int checkData(String data) {
		  BufferedReader br = null;
	        String line = "";
	        String cvsSplitBy = ",";
	        boolean compliant = true;
	        int nbSoutenances = 0;

	        try {

	            br = new BufferedReader(new FileReader(data));
	            br.readLine();
	            while (compliant && (line = br.readLine()) != null) {
	                // use comma as separator
	                String[] row = line.split(cvsSplitBy);
	            	if(row.length != 6) {
	            		//-1 means not right number of columns
	            		return -1;
	            	} else {
	            		nbSoutenances++;
	            	}

	            }
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            if (br != null) {
	                try {
	                    br.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
			
	        return nbSoutenances;
	}
	
	public int checkContraintes(String data) {
		  BufferedReader br = null;
	        String line = "";
	        String cvsSplitBy = ",";
	        
	        try {

	            br = new BufferedReader(new FileReader(data));
	            br.readLine();
	            int l = 0;//current line
	            while ((line = br.readLine()) != null) {
	                // use comma as separator
	                String[] row = line.split(cvsSplitBy);
	                if(l > 0) {
		                for(int i = 1; i < row.length -1; i++) {
		                	if(!row[i].equals("") && !row[i].equals("J") && !row[i].equals("AM") && !row[i].equals("M") && !row[i].equals("X")) {
		                		System.err.println("Problème à la ligne : '" + line + "'");
		                		return l;
		                	}
		                }
	                }
	                l++;
	            }
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            if (br != null) {
	                try {
	                    br.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	        return -1;
	}

	/**
	 * @param planning
	 * @param sallesSelectionnees 
	 * @param date 
	 * @param nbPeriodesParJour 
	 * @param impossibleAInserer 
	 * @throws IOException 
	 */
	public void writeData(Map<Integer, List<Creneau>> planning, ObservableList<String> sallesSelectionnees, Calendar date, int nbPeriodesParJour, List<Creneau> impossibleAInserer) throws IOException {
		StringBuilder sb = new StringBuilder();
		Set<Integer> periodes = planning.keySet();
		sb.append("sep=,\n");
		for(int periode : periodes) {
			if(periode%nbPeriodesParJour==0) {
				sb.append(",,,,,,,,,,,\n" + getDate(date) + ",");
				date.add(Calendar.DATE, 1);
				for(String salle : sallesSelectionnees) {
					sb.append(salle + ",,,,,");
				}
				sb.append("\n");
			}
			List<Creneau> creneaux = planning.get(periode);
			for(Creneau c : creneaux) {
					if(c.getSalle()==1) {
						sb.append(c.getHoraire() + ",");
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
		
		
		//System.out.println(sb.toString());
		Files.write(sb, new File(System.getProperty("user.home")+"/Downloads/generatedCSV.csv"), Charsets.UTF_8);
	}
	
	public String getDate(Calendar c) {
		return c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR);
	}

}