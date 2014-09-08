package com.alvaroy.promediouninorte.database;

import com.j256.ormlite.field.DatabaseField;

public class Subject {
	
	@DatabaseField(generatedId = true)
	private int id;	
	
	@DatabaseField(unique = true, canBeNull = false)
	private String name;
	
	@DatabaseField(canBeNull = false)
	private int credits;

	public Subject() {
	}

	public Subject(String name, int credits) {
		this.name = name;
		this.credits = credits;
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
	
}
