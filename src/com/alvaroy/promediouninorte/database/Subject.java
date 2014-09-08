package com.alvaroy.promediouninorte.database;

import com.j256.ormlite.field.DatabaseField;

public class Subject {
	
	@DatabaseField(generatedId = true)
	private int id;	
	
	@DatabaseField(canBeNull = false)
	private String name;
	
	@DatabaseField(canBeNull = false)
	private int credits;
	
	@DatabaseField(canBeNull = false)
	private int num_grades;

	public Subject() {
	}

	public Subject(String name, int credits, int num_grades) {
		this.name = name;
		this.credits = credits;
		this.num_grades = num_grades;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public int getCredits() {
		return credits;
	}

	public void setCredits(int credits) {
		this.credits = credits;
	}

	public int getNum_grades() {
		return num_grades;
	}

	public void setNum_grades(int num_grades) {
		this.num_grades = num_grades;
	}
	
}
