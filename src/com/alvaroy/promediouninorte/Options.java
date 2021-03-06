package com.alvaroy.promediouninorte;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class Options extends Fragment {
	
	Button cumAvg;
	Button cumSem;

	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		
		//Setting up fragment
		View rootView = inflater.inflate(R.layout.opciones, container, false);
		
		//Cumulative Average Button and method
		cumAvg = (Button) rootView.findViewById(R.id.cum_avg_button);
		cumAvg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle args = getArguments();
				PA_Calcular pa_calcular = new PA_Calcular();
				pa_calcular.setArguments(args);
				FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
				ft.replace(R.id.main, pa_calcular).addToBackStack("Options").commit();
			}
			
		});
		
		//Semester Average Button and method
		cumSem = (Button) rootView.findViewById(R.id.sem_avg_button);
		cumSem.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle args = getArguments();
				PM_Subject pm_subject = new PM_Subject();
				pm_subject.setArguments(args);
				FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
				ft.replace(R.id.main, pm_subject).addToBackStack("Options").commit();
			}
		});
		
		return rootView;
	}
	
	
	
}