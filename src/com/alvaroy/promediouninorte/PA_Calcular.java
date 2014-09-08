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
import android.widget.TextView;
import android.widget.Toast;

public class PA_Calcular extends Fragment {
	
	TextView txt;
	EditText avg;
	Button calc;
	View rootView;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		//Setting up fragment for view
		rootView = inflater.inflate(R.layout.pa_calcular, container, false);	
		calc = (Button) rootView.findViewById(R.id.calcular_calculate_button);
		avg = (EditText) rootView.findViewById(R.id.des_avg_edittxt);
		txt = (TextView) rootView.findViewById(R.id.cum_avg_txtview);
		
		//Database usage to get average 
		DatabaseHelper helper = OpenHelperManager.getHelper(rootView.getContext(), DatabaseHelper.class);
		RuntimeExceptionDao<Student, Integer> studentDAO = helper.getStudentRuntimeDAO();
		Student student = studentDAO.queryForEq("user", getArguments().getString("Username")).get(0);
		
		//Set average for view
		txt.setText(txt.getText().toString() + student.getCumulative_grade());
		
		//Button method
		calc.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				try {
					double desired = Double.valueOf(avg.getText().toString());
					if(desired >= 0.0 && desired <= 5.0) {
						Bundle args = getArguments();
						args.putDouble("AVG", desired);
						PA_ShowRes fragment = new PA_ShowRes();
						fragment.setArguments(args);
						FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
						ft.replace(R.id.main, fragment).addToBackStack("PA_Calcular").commit();
					}					
				}
				catch (NumberFormatException e) {				
					Toast.makeText(rootView.getContext(), "El dato tiene que ser numerico", Toast.LENGTH_SHORT).show();
				}
				
				
			}
		});
		
		return rootView;
	}
}
