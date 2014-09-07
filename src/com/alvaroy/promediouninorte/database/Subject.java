package com.alvaroy.promediouninorte.database;

import com.j256.ormlite.field.DatabaseField;

public class Subject {
	
	@DatabaseField(generatedId = true)
	private int id;	
	
	@DatabaseField(unique = true, canBeNull = false)
	private String name;
	
	@DatabaseField
	private double definite_grade;

	public Subject() {
	}

	public Subject(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getDefinite_grade() {
		return definite_grade;
	}

	public void setDefinite_grade(double definite_grade) {
		this.definite_grade = definite_grade;
	}

	public int getId() {
		return id;
	}
	
}
