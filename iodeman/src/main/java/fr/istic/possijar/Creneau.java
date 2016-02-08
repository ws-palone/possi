package fr.istic.possijar;
/**
 * 
 */

/**
 * @author François Esnault
 * @date 7 janv. 2016
 */
public class Creneau {
	private Enseignant enseignant;
	private Enseignant candide;
	private Tuteur tuteur;
	private Student student;
	private int periode;
	private int salle;
	
	public Creneau(int periode, Enseignant e, Enseignant c, Tuteur t, Student s) {
		this.periode = periode;
		this.enseignant = e;
		this.tuteur = t;
		this.candide = c;
		this.student = s;
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
		return this.periode + " " + this.enseignant + " " + this.tuteur + " " + this.candide + " " + this.student;
	}
	public String getHoraire() {
		int reste = periode%8;
		switch(reste) {
		case 0 : return "8h-9h";
		case 1 : return "9h-10h";
		case 2 : return "10h-11h";
		case 3 : return "11h-12h";
		case 4 : return "14h-15h";
		case 5 : return "15h-16h";
		case 6 : return "16h-17h";
		case 7 : return "17h-18h";
		default : return "";
		}
	}
	
}
