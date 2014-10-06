package com.alvaroy.promediouninorte;

import com.alvaroy.promediouninorte.database.DatabaseHelper;
import com.alvaroy.promediouninorte.database.Grade;
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

public class PM_OpsSubject extends Fragment {

	Button calc;
	Button edit;
	Button del;
	View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		//Set up fragment for use
		rootView = inflater.inflate(R.layout.pm_opssubject, container, false);
		calc = (Button) rootView.findViewById(R.id.opssubject_calc_grade);
		edit = (Button) rootView.findViewById(R.id.opsubject_edit_grade);
		
		//Check if calculate button should be enabled
		DatabaseHelper helper = OpenHelperManager.getHelper(rootView.getContext(), DatabaseHelper.class);
		RuntimeExceptionDao<StudentSubject, Integer> stusubDAO = helper.getStusubRuntimeDAO();
		RuntimeExceptionDao<Grade, Integer> gradeDAO = helper.getGradeRuntimeDAO();
		if(gradeDAO.queryForEq("stusub_id", stusubDAO.queryForId(getArguments().getInt("ID"))).isEmpty())
		calc.setEnabled(false);
		OpenHelperManager.releaseHelper();
		
		//Calculate grade button method
		calc.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle args = getArguments();
				PM_CalcSubject fragment = new PM_CalcSubject();
				fragment.setArguments(args);
				FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
				ft.replace(R.id.main, fragment).addToBackStack("PM_OpsSubject").commit();
			}
		});
		
		//Edit grades button method
		edit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle args = getArguments();
				PM_EditSubject fragment = new PM_EditSubject();
				fragment.setArguments(args);
				FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
				ft.replace(R.id.main, fragment).addToBackStack("PM_OpsSubject").commit();
			}
		});
		
		return rootView;
	}

}
