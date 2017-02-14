package fr.istic.possijar;

import java.io.Serializable;
/**
 * 
 */

/**
 * @author François Esnault
 * @date 7 janv. 2016
 */
public class Creneau implements Serializable {
	private Enseignant enseignant;
	private Enseignant candide;
	private Tuteur tuteur;
	private Student student;
	private int periode;
	private int salle;
	private String horaire;
	//list unav integer
	
	public Creneau(int periode, Enseignant e, Enseignant c, Tuteur t, Student s) {
		this.periode = periode;
		this.enseignant = e;
		this.tuteur = t;
		this.candide = c;
		this.student = s;
	}

	public Creneau(){

	}

	public Enseignant getEnseignant() {
		return enseignant;
	}
	public void setEnseignant(Enseignant enseignant) {
		this.enseignant = enseignant;
	}
	public Enseignant getCandide() {
		return candide;
	}
	public void setCandide(Enseignant candide) {
		this.candide = candide;
	}
	public Tuteur getTuteur() {
		return tuteur;
	}
	public void setTuteur(Tuteur tuteur) {
		this.tuteur = tuteur;
	}
	public int getPeriode() {
		return periode;
	}
	public void setPeriode(int periode) {
		this.periode = periode;
	}
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	
	public int getSalle() {
		return salle;
	}
	public void setSalle(int salle) {
		this.salle = salle;
	}
	public String toString() {
		return this.periode + " " + this.horaire + " " + this.enseignant + " " + this.tuteur + " " + this.candide + " " + this.student +" "+this.salle;
	}
	public String getHoraire() {
		System.err.println(horaire);
		String[] split = horaire.split(" ");
		if(split[1].length()==1) {
			split[1] = "0" + split[1];
		}
		if(split[2].length()==1) {
			split[2] = "0" + split[2];
		}
		return split[1] + "h" + split[2];
	}

	/**
	 * @param keyByValue
	 */
	public void setHoraire(String key) {
		horaire = key;
		System.err.println("Set " + horaire + " pour " + this);
	}
	
}
