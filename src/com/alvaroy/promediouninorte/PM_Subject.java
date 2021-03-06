package com.alvaroy.promediouninorte;

import java.util.ArrayList;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class PM_Subject extends Fragment {
	
	ArrayAdapter<String> adapter;
	ArrayList<Integer> ids = new ArrayList<Integer>();
	Button ing;
	ListView subject;
	View rootView;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		//Setup fragment for usage
		rootView = inflater.inflate(R.layout.pm_subject, container, false);
		subject = (ListView) rootView.findViewById(R.id.subject_list);
		ing = (Button) rootView.findViewById(R.id.add_subject_button);
		
		//Auto-load		
		makeList();
		AdapterAdapter adapt = new AdapterAdapter(rootView.getContext(), ids);
		subject.setAdapter(adapt);
		
		//ListView method to change fragment
		subject.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				Bundle args = getArguments();
				args.putInt("ID", ids.get(position));				
				PM_OpsSubject fragment = new PM_OpsSubject();
				fragment.setArguments(args);
				FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
				ft.replace(R.id.main, fragment).addToBackStack("PM_Subject").commit();
			}
		});
		
		ing.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle args = getArguments();
				PM_InsSubject fragment = new PM_InsSubject();
				fragment.setArguments(args);
				FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
				ft.replace(R.id.main, fragment).addToBackStack("PM_Subject").commit();
			}
		});
		
		return rootView;
	}
	
	private void makeList() {
		ids.clear();
		DatabaseHelper helper = OpenHelperManager.getHelper(rootView.getContext(), DatabaseHelper.class);
		RuntimeExceptionDao<Student, Integer> studentDAO = helper.getStudentRuntimeDAO();
		RuntimeExceptionDao<StudentSubject, Integer> stusubDAO = helper.getStusubRuntimeDAO();
		List<StudentSubject> names = stusubDAO.queryForEq("student_id", studentDAO.queryForEq("user", getArguments().getString("Username")).get(0).getId());
		for (StudentSubject stusub : names) {
			ids.add(stusub.getId());			
		}
		OpenHelperManager.releaseHelper();
	}

}
