package fr.istic.possijar;

import java.io.Serializable;
/**
 * 
 */

/**
 * @author François Esnault, Petit Emmanuel [M2 MIAGE]
 * @date 13 janv. 2016
 */
public class Student implements Serializable {
	
	private String name;
	private Enseignant enseignant;
	private Tuteur tuteur;
	
	public Student(String name, Enseignant e, Tuteur t) {
		this.name = name;
		this.enseignant = e;
		this.tuteur = t;
	}

	public Student(){

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Enseignant getEnseignant() {
		return enseignant;
	}

	public void setEnseignant(Enseignant enseignant) {
		this.enseignant = enseignant;
	}

	public Tuteur getTuteur() {
		return tuteur;
	}

	public void setTuteur(Tuteur tuteur) {
		this.tuteur = tuteur;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
	    if (obj == this) return true;
	    if (!(obj instanceof Student)) return false;
	    Student o = (Student) obj;
	    return o.name == this.name;
	}

}
