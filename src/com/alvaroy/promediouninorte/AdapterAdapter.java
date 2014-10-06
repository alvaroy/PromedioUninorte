package com.alvaroy.promediouninorte;

import java.util.List;

import com.alvaroy.promediouninorte.database.DatabaseHelper;
import com.alvaroy.promediouninorte.database.Grade;
import com.alvaroy.promediouninorte.database.StudentSubject;
import com.alvaroy.promediouninorte.database.Subject;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class AdapterAdapter extends BaseAdapter implements OnClickListener {
	
	private Context context;
	private List<Integer> listInfo;
	
	public AdapterAdapter (Context context, List<Integer> listInfo)
	{
		this.context = context;
		this.listInfo = listInfo;
	}

	@Override
	public int getCount() {
		return listInfo.size();
	}
	
	public Integer getItem(int position) {
		return listInfo.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup viewGroup) {
		
		final Integer entry = listInfo.get(position);
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.adapter, null);
		}
		
		final DatabaseHelper helper = OpenHelperManager.getHelper(
				context, DatabaseHelper.class);
		final RuntimeExceptionDao<StudentSubject, Integer> stusubDAO = helper.getStusubRuntimeDAO();
		final RuntimeExceptionDao<Subject, Integer> subjectDAO = helper.getSubjectRuntimeDAO();
		StudentSubject stusub = stusubDAO.queryForId(entry);
		
		TextView tvName = (TextView) convertView.findViewById(R.id.adapter_txt);
		tvName.setText(subjectDAO.queryForId(stusub.getSubject().getId()).getName());
		
		ImageButton img = (ImageButton) convertView.findViewById(R.id.adapter_img);
		img.setFocusableInTouchMode(false);
		img.setFocusable(false);
		img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LayoutInflater layoutInflaterPrompt = LayoutInflater.from(context);
				View promptView = layoutInflaterPrompt.inflate(R.layout.delete_promp, null);
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
				alertDialogBuilder.setView(promptView);
				alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Si", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								RuntimeExceptionDao<Grade, Integer> gradeDAO = helper.getGradeRuntimeDAO();
								List<Grade> grades = gradeDAO.queryForEq("stusub_id", entry);
								if(!grades.isEmpty()) {
									gradeDAO.delete(grades);
								}
								stusubDAO.deleteById(entry);
								OpenHelperManager.releaseHelper();
								listInfo.remove(position);
								notifyDataSetChanged();
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
		
		return convertView;
	}

	@Override
	public void onClick(View v) {
		
	}

}
