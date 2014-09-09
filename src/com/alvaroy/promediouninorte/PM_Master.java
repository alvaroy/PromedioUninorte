package com.alvaroy.promediouninorte;

import java.util.List;

import com.alvaroy.promediouninorte.database.DatabaseHelper;
import com.alvaroy.promediouninorte.database.Student;
import com.alvaroy.promediouninorte.database.StudentSubject;
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

public class PM_Master extends Fragment {

	Button calc;
	Button ing;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		//Set up fragment for use
		View rootView = inflater.inflate(R.layout.pm_master, container, false);
		calc = (Button) rootView.findViewById(R.id.calc_subject_button);
		ing = (Button) rootView.findViewById(R.id.add_subject_button);
		
		//Disable calc button if no subjects have been added for the user yet
		DatabaseHelper helper = OpenHelperManager.getHelper(rootView.getContext(), DatabaseHelper.class);
		RuntimeExceptionDao<Student, Integer> studentDAO = helper.getStudentRuntimeDAO();
		Student student = studentDAO.queryForEq("user", getArguments().getString("Username")).get(0);
		RuntimeExceptionDao<StudentSubject, Integer> stusubDAO = helper.getStusubRuntimeDAO();
		List<StudentSubject> total_subjects = stusubDAO.queryForEq("student_id", student.getId());
		if(total_subjects.isEmpty()) {
			calc.setEnabled(false);
		}
		
		//Calc button method to redirect to next fragment
		calc.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle args = getArguments();
				PM_Subject fragment = new PM_Subject();
				fragment.setArguments(args);
				FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
				ft.replace(R.id.main, fragment).addToBackStack("PM_Master").commit();
			}
			
		});
		
		//Ing button method to redirect to next fragment
		ing.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle args = getArguments();
				PM_InsSubject fragment = new PM_InsSubject();
				fragment.setArguments(args);
				FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
				ft.replace(R.id.main, fragment).addToBackStack("PM_Master").commit();
			}
		});
		
		return rootView;
	}

}
