package com.alvaroy.promediouninorte;

import java.sql.SQLException;

import com.alvaroy.promediouninorte.database.DatabaseHelper;
import com.alvaroy.promediouninorte.database.Grade;
import com.alvaroy.promediouninorte.database.Student;
import com.alvaroy.promediouninorte.database.StudentSubject;
import com.alvaroy.promediouninorte.database.Subject;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
		del = (Button) rootView.findViewById(R.id.opsubject_del_subject);
		
		//Check if calculate button should be enabled
		DatabaseHelper helper = OpenHelperManager.getHelper(rootView.getContext(), DatabaseHelper.class);
		RuntimeExceptionDao<Student, Integer> studentDAO = helper.getStudentRuntimeDAO();
		RuntimeExceptionDao<Subject, Integer> subjectDAO = helper.getSubjectRuntimeDAO();
		RuntimeExceptionDao<StudentSubject, Integer> stusubDAO = helper.getStusubRuntimeDAO();
		RuntimeExceptionDao<Grade, Integer> gradeDAO = helper.getGradeRuntimeDAO();
		Student student = studentDAO.queryForEq("user", getArguments().getString("Username")).get(0);
		try {
			Where<StudentSubject, Integer> query = stusubDAO.queryBuilder().where().eq("student_id", student).and().eq("subject_id", subjectDAO.queryForId(getArguments().getInt("ID")));
			if(gradeDAO.queryForEq("stusub_id", stusubDAO.query(query.prepare()).get(0)).isEmpty()) {				
				calc.setEnabled(false);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
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
		
		//Delete grades button method
		del.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LayoutInflater layoutInflaterPrompt = LayoutInflater.from(rootView.getContext());
				View promptView = layoutInflaterPrompt.inflate(R.layout.delete_promp, null);
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(rootView.getContext());
				alertDialogBuilder.setView(promptView);
				alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Si", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								DatabaseHelper helper = OpenHelperManager.getHelper(rootView.getContext(), DatabaseHelper.class);
								RuntimeExceptionDao<StudentSubject, Integer> stusub = helper.getStusubRuntimeDAO();
								stusub.deleteById(getArguments().getInt("ID"));
								OpenHelperManager.releaseHelper();
								getActivity().getSupportFragmentManager().popBackStack();
							}
						})
				.setNegativeButton("No",
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

}
