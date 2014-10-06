package com.alvaroy.promediouninorte;

import com.alvaroy.promediouninorte.database.DatabaseHelper;
import com.alvaroy.promediouninorte.database.Student;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PA_Calcular extends Fragment {

	EditText avg;
	EditText credits;
	EditText cum_avg;
	Button calc;
	View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Setting up fragment for view
		rootView = inflater.inflate(R.layout.pa_calcular, container, false);
		calc = (Button) rootView.findViewById(R.id.calcular_calculate_button);
		avg = (EditText) rootView.findViewById(R.id.des_avg_edittxt);
		credits = (EditText) rootView.findViewById(R.id.credits_completed_edit);
		cum_avg = (EditText) rootView.findViewById(R.id.cum_avg_edit);

		// Database usage to get average
		DatabaseHelper helper = OpenHelperManager.getHelper(
				rootView.getContext(), DatabaseHelper.class);
		RuntimeExceptionDao<Student, Integer> studentDAO = helper
				.getStudentRuntimeDAO();
		Student student = studentDAO.queryForEq("user",
				getArguments().getString("Username")).get(0);
		OpenHelperManager.releaseHelper();
		
		if(student.getCumulative_grade() != -1.0) {
			credits.setText(String.valueOf(student.getTotal_credits()));
			cum_avg.setText(String.valueOf(student.getCumulative_grade()));
		} 

		// Button method
		calc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!credits.getText().toString().isEmpty()
						&& !cum_avg.getText().toString().isEmpty()
						&& !avg.getText().toString().isEmpty()) {
					try {
						int credit = Integer.valueOf(credits.getText()
								.toString());
						double davg = Double.valueOf(cum_avg.getText()
								.toString());
						double desired = Double.valueOf(avg.getText()
								.toString());
						if (desired >= 0.0 && desired <= 5.0 && credit >= 0
								&& davg >= 0.0 && davg <= 5.0) {
							DatabaseHelper helper = OpenHelperManager
									.getHelper(rootView.getContext(),
											DatabaseHelper.class);
							RuntimeExceptionDao<Student, Integer> studentDAO = helper
									.getStudentRuntimeDAO();
							Student student = studentDAO.queryForEq("user",
									getArguments().getString("Username"))
									.get(0);
							student.setCumulative_grade(davg);
							student.setTotal_credits(credit);
							studentDAO.update(student);
							OpenHelperManager.releaseHelper();
							Bundle args = getArguments();
							args.putDouble("AVG", desired);
							PA_ShowRes fragment = new PA_ShowRes();
							fragment.setArguments(args);
							FragmentTransaction ft = getActivity()
									.getSupportFragmentManager()
									.beginTransaction();
							ft.replace(R.id.main, fragment)
									.addToBackStack("PA_Calcular").commit();
						} else {
							Toast.makeText(rootView.getContext(), "Los numeros tienen que estar entre 0 y 5", Toast.LENGTH_SHORT);
						}
					} catch (NumberFormatException e) {
						Toast.makeText(rootView.getContext(),
								"El dato tiene que ser numerico",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(rootView.getContext(),
							"Llene todos los campos", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});

		return rootView;
	}

}
