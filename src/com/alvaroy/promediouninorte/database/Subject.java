package com.alvaroy.promediouninorte.database;

import com.j256.ormlite.field.DatabaseField;

public class Subject {
	
	@DatabaseField(generatedId = true)
	private int id;	
	
	@DatabaseField(unique = true, canBeNull = false)
	private String name;

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

	public int getId() {
		return id;
	}
	
}
