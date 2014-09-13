package com.alvaroy.promediouninorte;

import com.alvaroy.promediouninorte.database.DatabaseHelper;
import com.alvaroy.promediouninorte.database.Student;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PA_IngDatos extends Fragment {
	
	Button save;
	EditText credits;
	EditText cum_avg;
	View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		//Setting up fragment
		rootView = inflater.inflate(R.layout.pa_ingdatos, container, false);
		save = (Button) rootView.findViewById(R.id.ing_datos_button);
		credits = (EditText) rootView.findViewById(R.id.credits_completed_edit);
		cum_avg = (EditText) rootView.findViewById(R.id.cum_avg_edit);
		
		//Button method
		save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(!credits.getText().toString().isEmpty() 
						|| !cum_avg.getText().toString().isEmpty()) {
					try {
						int credit = Integer.valueOf(credits.getText().toString());
						double avg = Double.valueOf(cum_avg.getText().toString());
						if(credit >= 0 && avg >= 0.0 && avg <= 5.0) {
							DatabaseHelper helper = OpenHelperManager.getHelper(rootView.getContext(), DatabaseHelper.class);
							RuntimeExceptionDao<Student, Integer> studentDAO = helper.getStudentRuntimeDAO();
							Student student = studentDAO.queryForEq("user", getArguments().getString("Username")).get(0);
							student.setCumulative_grade(avg);
							student.setTotal_credits(credit);
							studentDAO.update(student);
							OpenHelperManager.releaseHelper();
							getActivity().getSupportFragmentManager().popBackStack();
						}
						else {
							Toast.makeText(rootView.getContext(), "Valor invalido para estos campos", Toast.LENGTH_SHORT).show();
						}
					}
					catch (NumberFormatException e) {
						Toast.makeText(rootView.getContext(), "Ingrese solo valores numericos", Toast.LENGTH_SHORT).show();
					}
				}
				else {
					Toast.makeText(rootView.getContext(), "Llene todos los campos", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		return rootView;
	}
	
}
