package com.alvaroy.promediouninorte.database;

import com.j256.ormlite.field.DatabaseField;

public class StudentSubject {
	
	@DatabaseField(generatedId = true)
	private int id;
	
	@DatabaseField(foreign = true)
	private Student student;
	
	@DatabaseField(foreign = true)
	private Subject subject;
	
	@DatabaseField
	private double subject_grade;

	public StudentSubject() {
	}

	public StudentSubject(Student student, Subject subject) {
		this.student = student;
		this.subject = subject;
		this.subject_grade = -1.0;
	}

	public int getId() {
		return id;
	}

	public Student getStudent() {
		return student;
	}

	public Subject getSubject() {
		return subject;
	}

	public double getSubject_grade() {
		return subject_grade;
	}

	public void setSubject_grade(double subject_grade) {
		this.subject_grade = subject_grade;
	}
	
}
