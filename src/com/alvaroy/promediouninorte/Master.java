package com.alvaroy.promediouninorte;

import java.util.ArrayList;
import java.util.List;
import com.alvaroy.promediouninorte.database.DatabaseHelper;
import com.alvaroy.promediouninorte.database.Student;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class Master extends Fragment {
	
	ArrayList<String> list = new ArrayList<String>();
	ArrayAdapter<String> adapter;
	Button button;
	List<Student> stulist;
	ListView listV;
	View rootView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		//Setting up fragment for usage
		rootView = inflater.inflate(R.layout.master, container, false);
		
		//Query Database for student list
		DatabaseHelper helper = OpenHelperManager.getHelper(rootView.getContext(), DatabaseHelper.class);
		RuntimeExceptionDao<Student, Integer> studentDAO = helper.getStudentRuntimeDAO();
		stulist = studentDAO.queryForAll();		
		OpenHelperManager.releaseHelper();
		
		//Setting ListView adapter and method
		listV = (ListView) rootView.findViewById(R.id.student_list);
		adapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, list);
		updateList();
		listV.setAdapter(adapter);
		listV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Bundle args = new Bundle();
				args.putString("Username", list.get(position));
				Options options = new Options();
				options.setArguments(args);
				FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
				ft.replace(R.id.main, options).addToBackStack("Master").commit();
			}
			
		});
		
		//Button method
		button = (Button) rootView.findViewById(R.id.add_student);		
		button.setOnClickListener(new OnClickListener() {		
			
			public void onClick(View v) {
				LayoutInflater layoutInflaterPrompt = LayoutInflater.from(rootView.getContext());
				View promptView = layoutInflaterPrompt.inflate(R.layout.master_prompt, null);
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(rootView.getContext());
				alertDialogBuilder.setView(promptView);
				final EditText input = (EditText) promptView.findViewById(R.id.studentEditText);
				alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								if(!list.contains(input.getText().toString())) {
									DatabaseHelper helper = OpenHelperManager.getHelper(rootView.getContext(), DatabaseHelper.class);
									RuntimeExceptionDao<Student, Integer> studentDAO = helper.getStudentRuntimeDAO();
									Student student = new Student(input.getText().toString());
									studentDAO.create(student);
									stulist = studentDAO.queryForAll();
									OpenHelperManager.releaseHelper();
									updateList();
									adapter.notifyDataSetChanged();
								}
								else {
									Toast.makeText(rootView.getContext(), "Username already exists", Toast.LENGTH_SHORT).show();
								}
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,	int id) {
								dialog.cancel();
							}
						});
				AlertDialog alertD = alertDialogBuilder.create();
				alertD.show();
			}
			
		});
		
		return rootView;		
	}
	
	private void updateList() {
		list.clear();
		for (Student student : stulist) {
			list.add(student.getUser());
		}
	}
	
}