package com.alvaroy.promediouninorte.database;

import com.j256.ormlite.field.DatabaseField;

public class StudentSubject {
	
	@DatabaseField(generatedId = true)
	private int id;
	
	@DatabaseField(foreign = true)
	private Student student;
	
	@DatabaseField(foreign = true)
	private Subject subject;

	public StudentSubject() {
	}

	public StudentSubject(Student student, Subject subject) {
		this.student = student;
		this.subject = subject;
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

}
