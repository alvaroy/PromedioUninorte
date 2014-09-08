package com.alvaroy.promediouninorte.database;

import com.j256.ormlite.field.DatabaseField;

public class Grade {
	
	@DatabaseField(generatedId = true)
	private int id;
	
	@DatabaseField(canBeNull = false)
	private String name;
	
	@DatabaseField(canBeNull = false)
	private double percentage;
	
	@DatabaseField
	private double grade;
	
	@DatabaseField(foreign = true, canBeNull = false)
	private StudentSubject stusub;

	public Grade() {
	}

	public Grade(String name, double percentage, StudentSubject stusub) {
		this.name = name;
		this.percentage = percentage;
		this.stusub = stusub;
		this.grade = -1.0;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	public double getGrade() {
		return grade;
	}

	public void setGrade(double grade) {
		this.grade = grade;
	}

	public int getId() {
		return id;
	}

	public StudentSubject getStusub() {
		return stusub;
	}

}
