package com.alvaroy.promediouninorte.database;

import com.j256.ormlite.field.DatabaseField;

public class Student {
	
	@DatabaseField(generatedId = true)
	private int id;
	
	@DatabaseField(unique = true, canBeNull = false)
	private String user;
	
	@DatabaseField
	private double cumulative_grade;
	
	@DatabaseField
	private int total_credits;
		
	public Student() {
	}
	
	public Student(String user) {
		this.user = user;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public int getId() {
		return id;
	}
	
}