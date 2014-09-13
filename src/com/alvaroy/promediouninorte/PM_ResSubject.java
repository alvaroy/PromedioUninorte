package com.alvaroy.promediouninorte;

import java.util.List;
import com.alvaroy.promediouninorte.database.DatabaseHelper;
import com.alvaroy.promediouninorte.database.Grade;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class PM_ResSubject extends Fragment {

	LinearLayout line;
	TableLayout table;
	View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Set up fragment for use
		rootView = inflater.inflate(R.layout.pm_ressubject, container,
				false);
		table = (TableLayout) rootView.findViewById(R.id.result_table_subject);
		line = (LinearLayout) rootView.findViewById(R.id.linear_subject);

		// Get the desired average
		Double des = getArguments().getDouble("Desired");

		// Get the grades list for the subject
		DatabaseHelper helper = OpenHelperManager.getHelper(
				rootView.getContext(), DatabaseHelper.class);
		RuntimeExceptionDao<Grade, Integer> gradeDAO = helper
				.getGradeRuntimeDAO();
		List<Grade> grades = gradeDAO.queryForEq("stusub_id", getArguments()
				.getInt("ID"));
		double total = 0;
		double pct = 0;
		for (Grade grade : grades) {
			if (grade.getGrade() != -1.0) {
				total += grade.getGrade() * (grade.getPercentage() / 100);
			} else {
				pct += grade.getPercentage();
			}
		}
		//Check if student already has the desired average
		if (total >= des) {			
			TextView txt = new TextView(rootView.getContext());
			txt.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			txt.setText("Su promedio ya es igual o superior a la nota deseada");			
			txt.setTextSize(22);
			line.addView(txt);
		//Check if he still can get the desired average
		} else {
			//No possibility of getting the desired average
			if(pct == 0) {
				TextView txt = new TextView(rootView.getContext());
				txt.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT));
				txt.setText("No hay posibilidad de que su promedio llegue a la nota deseada con sus notas actuales");				
				txt.setTextSize(22);
				line.addView(txt);
			//Still possible to get the desired average	
			} else {
				double need = calcDes(des, total, pct);
				//Calculations don't give a realistic number
				if (need < 0.0 || need > 5.0) {
					TextView txt = new TextView(rootView.getContext());
					txt.setText("No hay posibilidad de que su promedio llegue a la nota deseada con sus notas actuales");
					txt.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
					txt.setTextSize(22);					
					line.addView(txt);
				//It IS possible to get the student desired grade going by calculations
				} else {
					addHeaders();
					fillRows(grades, need);
				}
				
			}
		}
		return rootView;
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
		// Add grade header
		TextView grade = new TextView(rootView.getContext());
		grade.setText("Puntaje");
		grade.setTextColor(Color.WHITE);
		tr_head.addView(grade);
		table.addView(tr_head, new TableLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}
	
	private void fillRows(List<Grade> grades, double need) {
		for (Grade grade : grades) {
			TextView name = new TextView(rootView.getContext());
			TextView gradetxt = new TextView(rootView.getContext());
			name.setText(grade.getName());
			if(grade.getGrade() == -1.0) {
				gradetxt.setText(String.valueOf(need));
				gradetxt.setTextColor(Color.RED);
			} else {
				gradetxt.setText(String.valueOf(grade.getGrade()));
			}
			TableRow tr = new TableRow(rootView.getContext());
			tr.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));
			tr.addView(name);
			tr.addView(gradetxt);
			table.addView(tr);
		}
	}
	
	private double calcDes(double des, double total, double pct) {
		double need = des - total;
		need = need * 100 / pct;
		return need;
	}

}
