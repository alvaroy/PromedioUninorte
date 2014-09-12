package com.alvaroy.promediouninorte;

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

public class PM_CalcSubject extends Fragment {

	View rootView;
	EditText txt;
	Button btn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Set up fragment for use
		rootView = inflater.inflate(R.layout.pm_calcsubject, container, false);
		txt = (EditText) rootView.findViewById(R.id.calcsubject_edittext);
		btn = (Button) rootView.findViewById(R.id.calcsubject_button);

		// Set up button method
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				double desired = Double.valueOf(txt.getText().toString());
				if (desired >= 0.0 && desired <= 5.0) {
					Bundle args = getArguments();
					args.putDouble("Desired", desired);
					PM_ResSubject fragment = new PM_ResSubject();
					fragment.setArguments(args);
					FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
					ft.replace(R.id.main, fragment).addToBackStack("PM_CalcSubject").commit();
				}
				else if (desired > 5.0 ){
					Toast.makeText(rootView.getContext(), "La nota no puede ser mas de 5", Toast.LENGTH_SHORT).show();
				}
				else { 
					Toast.makeText(rootView.getContext(), "La nota no puede ser menos de 0", Toast.LENGTH_SHORT).show();
				}
			}
		});

		return rootView;
	}

}
