package com.alvaroy.promediouninorte;

import java.util.ArrayList;
import java.util.List;

import com.alvaroy.promediouninorte.database.DatabaseHelper;
import com.alvaroy.promediouninorte.database.Student;
import com.alvaroy.promediouninorte.database.StudentSubject;
import com.alvaroy.promediouninorte.database.Subject;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PM_Subject extends Fragment {
	
	ArrayAdapter<String> adapter;
	ListView subject;
	View rootView;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		//Setup fragment for usage
		rootView = inflater.inflate(R.layout.pm_subject, container, false);
		subject = (ListView) rootView.findViewById(R.id.subject_list);
		
		//Auto-load		
		ArrayList<String> list = makeList();
		adapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, list);
		subject.setAdapter(adapter);
		
		return rootView;
	}
	
	private ArrayList<String> makeList() {
		ArrayList<String> list = new ArrayList<String>();
		DatabaseHelper helper = OpenHelperManager.getHelper(rootView.getContext(), DatabaseHelper.class);
		RuntimeExceptionDao<Student, Integer> studentDAO = helper.getStudentRuntimeDAO();
		RuntimeExceptionDao<Subject, Integer> subjectDAO = helper.getSubjectRuntimeDAO();
		RuntimeExceptionDao<StudentSubject, Integer> stusubDAO = helper.getStusubRuntimeDAO();
		List<StudentSubject> names = stusubDAO.queryForEq("student_id", studentDAO.queryForEq("user", getArguments().getString("Username")));
		for (StudentSubject stusub : names) {
			list.add(String.valueOf(stusub.getSubject().getId()));
		}
		return list;
	}

}
