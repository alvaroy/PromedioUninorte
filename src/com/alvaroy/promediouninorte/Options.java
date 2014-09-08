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
	String username;

	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		
		//Setting up fragment
		View rootView = inflater.inflate(R.layout.opciones, container, false);
		
		//Get Username
		username = getArguments().getString("Username");
		
		//Cumulative Average Button and method
		cumAvg = (Button) rootView.findViewById(R.id.cum_avg_button);
		cumAvg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle args = new Bundle();
				args.putString("Username", username);
				PA_Master pa_master = new PA_Master();
				pa_master.setArguments(args);
				FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
				ft.replace(R.id.main, pa_master).addToBackStack("Options").commit();
			}
			
		});
		
		//Semester Average Button and method
		cumSem = (Button) rootView.findViewById(R.id.sem_avg_button);
		cumSem.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle args = new Bundle();
				args.putString("Username", username);
				PM_Master pm_master = new PM_Master();
				pm_master.setArguments(args);
				FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
				ft.replace(R.id.main, pm_master).addToBackStack("Options").commit();
			}
		});
		
		return rootView;
	}
	
	
	
}