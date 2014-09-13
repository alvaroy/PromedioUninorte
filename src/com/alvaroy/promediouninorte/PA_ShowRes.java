package com.alvaroy.promediouninorte;

import java.util.List;
import com.alvaroy.promediouninorte.database.DatabaseHelper;
import com.alvaroy.promediouninorte.database.Student;
import com.alvaroy.promediouninorte.database.StudentSubject;
import com.alvaroy.promediouninorte.database.Subject;
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

public class PA_ShowRes extends Fragment {

	TableLayout table;
	LinearLayout line;
	View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Set up fragment for use
		rootView = inflater.inflate(R.layout.pa_showres, container, false);
		table = (TableLayout) rootView.findViewById(R.id.showres_table);
		line = (LinearLayout) rootView.findViewById(R.id.showres_linear);

		// Get database info
		DatabaseHelper helper = OpenHelperManager.getHelper(
				rootView.getContext(), DatabaseHelper.class);
		RuntimeExceptionDao<Student, Integer> studentDAO = helper
				.getStudentRuntimeDAO();
		RuntimeExceptionDao<StudentSubject, Integer> stusubDAO = helper
				.getStusubRuntimeDAO();
		RuntimeExceptionDao<Subject, Integer> subjectDAO = helper
				.getSubjectRuntimeDAO();
		Student student = studentDAO.queryForEq("user",
				getArguments().getString("Username")).get(0);
		List<StudentSubject> stusubL = stusubDAO.queryForEq("student_id",
				student.getId());

		// Calculate total credit value
		double value = student.getCumulative_grade()
				* student.getTotal_credits();

		// Calculate free credits and assigned credit value
		int free = 0;
		int scredits = 0;
		double cvalue = 0;
		for (StudentSubject stusub : stusubL) {
			// Student has a subject grade defined by his grade
			if (stusub.getSubject_grade() != -1.0) {
				cvalue += stusub.getSubject_grade()
						* subjectDAO.queryForId(stusub.getSubject().getId())
								.getCredits();
				// Student free credits to try get his desired average
			} else {
				free += subjectDAO.queryForId(stusub.getSubject().getId())
						.getCredits();
			}
			scredits += subjectDAO.queryForId(stusub.getSubject().getId())
					.getCredits();
		}
		value += cvalue;

		// Student already has his desired average
		if (value / (student.getTotal_credits() + scredits) >= getArguments()
				.getDouble("AVG")) {
			TextView txt = new TextView(rootView.getContext());
			txt.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));
			txt.setText("Su promedio ya es igual o superior a la nota deseada");
			txt.setTextSize(22);
			line.addView(txt);
			// Check if he can achieve his desired average
		} else {
			// Average is lower than desired and no credits available
			if (free == 0) {
				TextView txt = new TextView(rootView.getContext());
				txt.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT));
				txt.setText("No hay posibilidad de que su promedio llegue a la nota deseada con sus notas actuales");
				txt.setTextSize(22);
				line.addView(txt);
			} else {
				double need = ((getArguments().getDouble("AVG") * (student
						.getTotal_credits() + scredits)) - value);
				// No chance of getting the desired average
				if (need > (free * 5)) {
					TextView txt = new TextView(rootView.getContext());
					txt.setText("No hay posibilidad de que su promedio llegue a la nota deseada con sus notas actuales");
					txt.setLayoutParams(new LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
					txt.setTextSize(22);
					line.addView(txt);
					// Possible to get
				} else {
					addHeaders();
					need = need / free;
					fillRows(stusubL, need, stusubDAO, subjectDAO);
				}
			}
		}

		OpenHelperManager.releaseHelper();
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
		name.setTextSize(22);
		name.setPadding(15, 0, 0, 0);
		tr_head.addView(name);
		// Add grade header
		TextView grade = new TextView(rootView.getContext());
		grade.setText("Puntaje");
		grade.setTextColor(Color.WHITE);
		grade.setTextSize(22);
		grade.setPadding(15, 0, 0, 0);
		tr_head.addView(grade);
		table.addView(tr_head, new TableLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}

	private void fillRows(List<StudentSubject> stusubs, double need,
			RuntimeExceptionDao<StudentSubject, Integer> stusubDAO,
			RuntimeExceptionDao<Subject, Integer> subjectDAO) {
		for (StudentSubject stusub : stusubs) {
			TextView name = new TextView(rootView.getContext());
			TextView gradetxt = new TextView(rootView.getContext());
			name.setText(subjectDAO.queryForId(stusub.getSubject().getId()).getName());
			name.setTextSize(20);
			name.setPadding(15, 0, 0, 0);
			if (stusub.getSubject_grade() == -1.0) {
				gradetxt.setText(String.valueOf(need));
				gradetxt.setTextColor(Color.RED);
			} else {
				gradetxt.setText(String.valueOf(stusub.getSubject_grade()));
			}
			gradetxt.setTextSize(20);
			gradetxt.setPadding(15, 0, 0, 0);
			TableRow tr = new TableRow(rootView.getContext());
			tr.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));
			tr.addView(name);
			tr.addView(gradetxt);
			table.addView(tr);
		}
	}

}
