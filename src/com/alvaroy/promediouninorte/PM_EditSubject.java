package com.alvaroy.promediouninorte;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.alvaroy.promediouninorte.database.DatabaseHelper;
import com.alvaroy.promediouninorte.database.Grade;
import com.alvaroy.promediouninorte.database.Student;
import com.alvaroy.promediouninorte.database.StudentSubject;
import com.alvaroy.promediouninorte.database.Subject;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.Where;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class PM_EditSubject extends Fragment {

	TableLayout table;
	View rootView;
	Button save;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Set up fragment for use
		rootView = inflater.inflate(R.layout.pm_editsubject, container, false);
		table = (TableLayout) rootView.findViewById(R.id.edit_table);
		save = (Button) rootView.findViewById(R.id.edit_subject_button);

		// Fill table
		fillTable();

		// Button method
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Find the student subject for saving
				DatabaseHelper helper = OpenHelperManager.getHelper(
						rootView.getContext(), DatabaseHelper.class);
				RuntimeExceptionDao<StudentSubject, Integer> stusubDAO = helper
						.getStusubRuntimeDAO();
				StudentSubject stusub = stusubDAO.queryForId(getArguments()
						.getInt("ID"));
				int err = 0;
				double total_pct = 0;
				ArrayList<Double> pctgrade = new ArrayList<Double>();
				ArrayList<Grade> grades = new ArrayList<Grade>();
				// Go over the table
				for (int i = 1; i < table.getChildCount(); i++) {
					String name = "";
					double pct = -1.0;
					double grade = -1.0;
					int id = -1;
					TableRow tr = (TableRow) table.getChildAt(i);
					EditText et;
					// Check for error at first EditText
					et = (EditText) tr.getChildAt(0);
					if (et.getText().toString().trim().isEmpty()) {
						err = 1;
						break;
					} else {
						name = et.getText().toString();
						if (et.getTag() != null) {
							id = (Integer) et.getTag();
						}
					}
					// Check for error at second EditText
					et = (EditText) tr.getChildAt(1);
					if (et.getText().toString().trim().isEmpty()) {
						err = 2;
						break;
					} else {
						pct = Double.valueOf(et.getText().toString());
						total_pct += pct;
					}
					// Check for error at third EditText
					et = (EditText) tr.getChildAt(2);
					if (!et.getText().toString().trim().isEmpty()) {
						if (Double.valueOf(et.getText().toString().trim()) < 0.0
								|| Double.valueOf(et.getText().toString()
										.trim()) > 5.0) {
							err = 3;
							break;
						} else {
							grade = Double.valueOf(et.getText().toString());
						}
					}
					grades.add(new Grade(name, pct, stusub));
					Grade sgrade = grades.get(i - 1);
					sgrade.setId(id);
					if (grade >= 0.0) {
						sgrade.setGrade(grade);
						pctgrade.add(grade);
					}
					grades.set(i - 1, sgrade);
				}
				if (err == 0) {
					if (total_pct == 100) {
						if (pctgrade.size() == grades.size()) {
							stusub.setSubject_grade(calcAVG(pctgrade, grades));
							stusubDAO.update(stusub);
						}
						for (Grade grade : grades) {
							RuntimeExceptionDao<Grade, Integer> gradeDAO = helper
									.getGradeRuntimeDAO();
							// Create if not exists
							if (grade.getId() == -1) {
								gradeDAO.create(grade);
								// Update if it exists
							} else {
								gradeDAO.update(grade);
							}
						}
					} else {
						Toast.makeText(rootView.getContext(),
								"Porcentajes deben sumar 100",
								Toast.LENGTH_SHORT).show();
					}
				} else if (err == 1) {
					Toast.makeText(rootView.getContext(),
							"Todos los titulos deben estar completos",
							Toast.LENGTH_SHORT).show();
				} else if (err == 2) {
					Toast.makeText(rootView.getContext(),
							"Todos los porcentajes deben estar completos",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(rootView.getContext(),
							"La nota maxima es 5.0", Toast.LENGTH_SHORT).show();
				}
				OpenHelperManager.releaseHelper();
			}
		});

		return rootView;
	}

	// Method to automatically fill the table
	private void fillTable() {
		// First row
		addHeaders();
		// Rest of the rows
		fillRows();
	}

	private void addHeaders() {
		TableRow tr_head = new TableRow(rootView.getContext());
		tr_head.setBackgroundColor(Color.GRAY);
		tr_head.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		// Add name header
		TextView name = new TextView(rootView.getContext());
		name.setText("Titulo");
		name.setTextColor(Color.WHITE);
		tr_head.addView(name);
		// Add percentage header
		TextView pct = new TextView(rootView.getContext());
		pct.setText("Porcentaje");
		pct.setTextColor(Color.WHITE);
		tr_head.addView(pct);
		// Add grade header
		TextView grade = new TextView(rootView.getContext());
		grade.setText("Puntaje");
		grade.setTextColor(Color.WHITE);
		tr_head.addView(grade);
		table.addView(tr_head, new TableLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}

	private void fillRows() {
		DatabaseHelper helper = OpenHelperManager.getHelper(
				rootView.getContext(), DatabaseHelper.class);
		RuntimeExceptionDao<Subject, Integer> subjectDAO = helper
				.getSubjectRuntimeDAO();
		RuntimeExceptionDao<StudentSubject, Integer> stusubDAO = helper
				.getStusubRuntimeDAO();
		RuntimeExceptionDao<Grade, Integer> gradeDAO = helper
				.getGradeRuntimeDAO();
		StudentSubject stusub = stusubDAO.queryForId(getArguments()
				.getInt("ID"));
		List<Grade> grades = gradeDAO.queryForEq("stusub_id", stusub);
		for (int i = 0; i < subjectDAO.queryForId(stusub.getSubject().getId())
				.getNum_grades(); i++) {
			EditText name = new EditText(rootView.getContext());
			name.setInputType(InputType.TYPE_CLASS_TEXT
					| InputType.TYPE_TEXT_FLAG_CAP_WORDS);
			EditText pct = new EditText(rootView.getContext());
			pct.setInputType(InputType.TYPE_CLASS_NUMBER
					| InputType.TYPE_NUMBER_FLAG_DECIMAL);
			EditText grade = new EditText(rootView.getContext());
			grade.setInputType(InputType.TYPE_CLASS_NUMBER
					| InputType.TYPE_NUMBER_FLAG_DECIMAL);
			if (grades.isEmpty()) {
				name.setText("Nota".concat(String.valueOf(i + 1)));
			} else {
				name.setText(grades.get(i).getName());
				name.setTag(grades.get(i).getId());
				pct.setText(String.valueOf(grades.get(i).getPercentage()));
				if (grades.get(i).getGrade() != -1.0) {
					grade.setText(String.valueOf(grades.get(i).getGrade()));
				}
			}
			TableRow tr = new TableRow(rootView.getContext());
			tr.addView(name);
			tr.addView(pct);
			tr.addView(grade);
			table.addView(tr);
		}
	}

	private double calcAVG(ArrayList<Double> grades, ArrayList<Grade> pct) {
		double total = 0;
		for (int i = 0; i < grades.size(); i++) {
			total += grades.get(i) * (pct.get(i).getPercentage() / 100);
		}
		return total;
	}

}
