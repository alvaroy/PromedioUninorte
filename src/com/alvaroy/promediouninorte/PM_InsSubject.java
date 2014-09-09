package com.alvaroy.promediouninorte;

import com.alvaroy.promediouninorte.database.DatabaseHelper;
import com.alvaroy.promediouninorte.database.Subject;
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

public class PM_InsSubject extends Fragment {

	EditText name;
	EditText credits;
	EditText grades;
	Button save;
	View rootView;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		//Setup fragment for usage
		rootView = inflater.inflate(R.layout.pm_inssubject, container, false);
		name = (EditText) rootView.findViewById(R.id.mat_name_edit);
		credits = (EditText) rootView.findViewById(R.id.credits_done_edit);
		grades = (EditText) rootView.findViewById(R.id.grades_totel_edit);
		save = (Button) rootView.findViewById(R.id.pm_inssubject_button);
		
		//Save button method
		save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String nametxt = name.getText().toString();
				String creditstxt = credits.getText().toString();
				String gradestxt = grades.getText().toString();
				if(!nametxt.isEmpty() && !creditstxt.isEmpty() && !gradestxt.isEmpty()) {
					try {
						Subject subject = new Subject(nametxt, Integer.parseInt(creditstxt), Integer.parseInt(gradestxt));
						DatabaseHelper helper = OpenHelperManager.getHelper(rootView.getContext(), DatabaseHelper.class);
						RuntimeExceptionDao<Subject, Integer> subjectDAO = helper.getSubjectRuntimeDAO();
						subjectDAO.create(subject);
						OpenHelperManager.releaseHelper();
						getActivity().getSupportFragmentManager().popBackStack();
					}
					catch (NumberFormatException e) {
						Toast.makeText(rootView.getContext(), "Los numeros no son validos", Toast.LENGTH_SHORT).show();
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
