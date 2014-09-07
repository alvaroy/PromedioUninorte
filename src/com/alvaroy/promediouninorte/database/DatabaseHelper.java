package com.alvaroy.promediouninorte.database;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.alvaroy.promediouninorte.R;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
	
	//Database Information
	private static final String DATABASE_NAME = "student_grades.db";
	private static final int DATABASE_VERSION = 1;
	
	//Tables DAO
	private Dao<Student, Integer> studentDAO = null;
	private RuntimeExceptionDao<Student, Integer> studentRuntimeDAO = null;
	
	private Dao<Grade, Integer> gradeDAO = null;
	private RuntimeExceptionDao<Grade, Integer> gradeRuntimeDAO = null;
	
	private Dao<Subject, Integer> subjectDAO = null;
	private RuntimeExceptionDao<Subject, Integer> subjectRuntimeDAO = null;
	
	private Dao<StudentSubject, Integer> stusubDAO = null;
	private RuntimeExceptionDao<StudentSubject, Integer> stusubRuntimeDAO = null;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
	}

	@Override
	public void onCreate(SQLiteDatabase database,
			ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, Student.class);
			TableUtils.createTable(connectionSource, Grade.class);
			TableUtils.createTable(connectionSource, Subject.class);
			TableUtils.createTable(connectionSource, StudentSubject.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase database,
			ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			TableUtils.dropTable(connectionSource, Student.class, true);
			TableUtils.dropTable(connectionSource, Grade.class, true);
			TableUtils.dropTable(connectionSource, Subject.class, true);
			TableUtils.dropTable(connectionSource, StudentSubject.class, true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void close() {
		super.close();
		studentDAO = null;
		studentRuntimeDAO = null;
		stusubDAO = null;
		stusubRuntimeDAO = null;
		gradeDAO = null;
		gradeRuntimeDAO = null;
		subjectDAO = null;
		subjectRuntimeDAO = null;
	}

	public Dao<Student, Integer> getStudentDAO() throws SQLException {
		if(studentDAO == null) studentDAO = getDao(Student.class);
		return studentDAO;
	}

	public RuntimeExceptionDao<Student, Integer> getStudentRuntimeDAO() {
		if(studentRuntimeDAO == null) studentRuntimeDAO = getRuntimeExceptionDao(Student.class);
		return studentRuntimeDAO;
	}

	public Dao<Grade, Integer> getGradeDAO() throws SQLException {
		if(gradeDAO == null) gradeDAO = getDao(Grade.class);
		return gradeDAO;
	}

	public RuntimeExceptionDao<Grade, Integer> getGradeRuntimeDAO() {
		if(gradeRuntimeDAO == null) gradeRuntimeDAO = getRuntimeExceptionDao(Grade.class);
		return gradeRuntimeDAO;
	}

	public Dao<Subject, Integer> getSubjectDAO() throws SQLException {
		if(subjectDAO == null) subjectDAO = getDao(Subject.class);
		return subjectDAO;
	}

	public RuntimeExceptionDao<Subject, Integer> getSubjectRuntimeDAO() {
		if(subjectRuntimeDAO == null) subjectRuntimeDAO = getRuntimeExceptionDao(Subject.class);
		return subjectRuntimeDAO;
	}

	public Dao<StudentSubject, Integer> getStusubDAO() throws SQLException {
		if(stusubDAO == null) stusubDAO = getDao(StudentSubject.class);
		return stusubDAO;
	}

	public RuntimeExceptionDao<StudentSubject, Integer> getStusubRuntimeDAO() {
		if(stusubRuntimeDAO == null) stusubRuntimeDAO = getRuntimeExceptionDao(StudentSubject.class);
		return stusubRuntimeDAO;
	}
			
}
