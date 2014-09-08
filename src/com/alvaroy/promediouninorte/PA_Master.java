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

public class PA_Master extends Fragment {
	
	Button avg;
	Button ins;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		//Get fragment ready
		View rootView = inflater.inflate(R.layout.pa_master, container, false);
		avg = (Button) rootView.findViewById(R.id.calc_avg_button);
		ins = (Button) rootView.findViewById(R.id.insert_data_button);
		
		//Database stuff
		DatabaseHelper helper = OpenHelperManager.getHelper(rootView.getContext(), DatabaseHelper.class);
		RuntimeExceptionDao<Student, Integer> studentDAO = helper.getStudentRuntimeDAO();
		Student student = studentDAO.queryForEq("user", getArguments().getString("Username")).get(0);
		OpenHelperManager.releaseHelper();
		if(student.getCumulative_grade() == -1.0) {
			avg.setEnabled(false);
		}
		
		//Button methods
		avg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle args = getArguments();
				PA_Calcular pa_calcular = new PA_Calcular();
				pa_calcular.setArguments(args);
				FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
				ft.replace(R.id.main, pa_calcular).addToBackStack("PA_Master").commit();
			}
		});
		
		ins.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle args = getArguments();
				PA_IngDatos pa_ingdatos = new PA_IngDatos();
				pa_ingdatos.setArguments(args);
				FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
				ft.replace(R.id.main, pa_ingdatos).addToBackStack("PA_Master").commit();
			}
		});
		
		return rootView;
	}

}
