package com.alvaroy.promediouninorte;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.alvaroy.promediouninorte.database.DatabaseHelper;
import com.alvaroy.promediouninorte.database.Grade;
import com.alvaroy.promediouninorte.database.Student;
import com.alvaroy.promediouninorte.database.StudentSubject;
import com.alvaroy.promediouninorte.database.Subject;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.Where;
import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class PM_InsSubject extends Fragment {

	String TAG = getTag();
	Spinner name;
	EditText credits;
	EditText grades;
	Button save;
	protected JSONObject mData;
	protected ArrayList<Integer> mCount;
	View rootView;
	String my_var;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		//Setup fragment for usage
		rootView = inflater.inflate(R.layout.pm_inssubject, container, false);
		name = (Spinner) rootView.findViewById(R.id.mat_name_edit);
		credits = (EditText) rootView.findViewById(R.id.credits_done_edit);
		grades = (EditText) rootView.findViewById(R.id.grades_totel_edit);
		save = (Button) rootView.findViewById(R.id.pm_inssubject_button);
		save.setEnabled(false);
		
		name.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				grades.setText(mCount.get(arg2).toString());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				//Nothing
			}
			
		});
				
		//Save button method
		save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String nametxt = name.getSelectedItem().toString();	
				String creditstxt = credits.getText().toString();
				String gradestxt = grades.getText().toString();
				if(!nametxt.isEmpty() && !creditstxt.isEmpty() && !gradestxt.isEmpty()) {
					try {
						DatabaseHelper helper = OpenHelperManager.getHelper(rootView.getContext(), DatabaseHelper.class);
						RuntimeExceptionDao<Student, Integer> studentDAO = helper.getStudentRuntimeDAO();
						RuntimeExceptionDao<Subject, Integer> subjectDAO = helper.getSubjectRuntimeDAO();
						RuntimeExceptionDao<StudentSubject, Integer> stusubDAO = helper.getStusubRuntimeDAO();
						RuntimeExceptionDao<Grade, Integer> gradeDAO = helper.getGradeRuntimeDAO();
						Subject subject = new Subject(nametxt, Integer.parseInt(creditstxt), Integer.parseInt(gradestxt));	
						if(subjectDAO.queryForMatching(subject).isEmpty()) {
							subjectDAO.create(subject);
						}	
						StudentSubject stusub = new StudentSubject(
								/*Student*/studentDAO.queryForEq("user", getArguments().getString("Username")).get(0)
								/*Subject*/,subjectDAO.queryForMatching(subject).get(0));
						Where<StudentSubject,Integer> query = stusubDAO.queryBuilder().where().eq("student_id", stusub.getStudent().getId()).and()
							.eq("subject_id", stusub.getSubject().getId());
						if(stusubDAO.query(query.prepare()).isEmpty()) {
							stusubDAO.create(stusub);
							//..........................//
							try {
								JSONArray jsonPosts = mData.getJSONArray("materias");
								for (int i = 0;i< jsonPosts.length();i++){
									JSONObject post = jsonPosts.getJSONObject(i);
									String title = post.getString("nombre_materia");
									title  = Html.fromHtml(title).toString();
									String periodo = post.getString("periodo");
									periodo = Html.fromHtml(periodo).toString();
									if(name.getSelectedItem().toString().equals(title+"_"+periodo)) {
										JSONArray grades = post.getJSONArray("componetes");
										for (int j = 0; j < grades.length(); j++) {
											JSONObject grade = grades.getJSONObject(j);
											gradeDAO.create(new Grade(Html.fromHtml(grade.getString("desc")).toString(), 
													grade.getDouble("peso"), 
													stusub));
										}										
									}	
								}
							} catch (JSONException e) {
							}
							//..........................//
							OpenHelperManager.releaseHelper();
							getActivity().getSupportFragmentManager().popBackStack();
						} 
						else {
							Toast.makeText(rootView.getContext(), "Ya tiene esta materia registrada", Toast.LENGTH_SHORT).show();
							OpenHelperManager.releaseHelper();
						}
					}
					catch (NumberFormatException e) {
						Toast.makeText(rootView.getContext(), "Los numeros no son validos", Toast.LENGTH_SHORT).show();
						OpenHelperManager.releaseHelper();
					}
					catch (SQLException s) {
						OpenHelperManager.releaseHelper();
					}
				}
				else {
					Toast.makeText(rootView.getContext(), "Llene todos los campos", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		return rootView;
	}
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	
		if (isNetworkAvailable()) {
			GetDataTask getDataTask = new GetDataTask();
			getDataTask.execute();
		} else {
			save.setEnabled(false);
		}
    }
	
	private boolean isNetworkAvailable() {
		rootView.getContext();
		ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		boolean isNetworkAvaible = false;
		if (networkInfo != null && networkInfo.isConnected()) {
			isNetworkAvaible = true;
			Toast.makeText(rootView.getContext(), "Network is available ", Toast.LENGTH_LONG)
					.show();
		} else {
			Toast.makeText(rootView.getContext(), "Network not available ", Toast.LENGTH_LONG)
					.show();
		}
		return isNetworkAvaible;
	}
	
	public class GetDataTask extends AsyncTask<Object, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(Object... params) {
			InputStream is = null;
			String result = "";
			JSONObject jsonObject = null;
			
			// HTTP
			try {	    	
				HttpClient httpclient = new DefaultHttpClient(); // for port 80 requests!
				HttpGet httppost = new HttpGet("http://augustodesarrollador.com/promedio_app/read.php");
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
			} catch(Exception e) {
				return null;
			}
		    
			// Read response to string
			try {	    	
				BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"),8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				result = sb.toString();	            
			} catch(Exception e) {
				return null;
			}
	 
			// Convert string to object
			try {
				jsonObject = new JSONObject(result);            
			} catch(JSONException e) {
				return null;
			}
	    
			return jsonObject;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			mData = result;
			handleBlogResponse();
		}

	}

	public void handleBlogResponse() {
		if (mData == null){
			AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
			builder.setTitle("Error");
			builder.setMessage("Ocurrio un error, los datos no fueron descargados");
			builder.setPositiveButton(android.R.string.ok, null);
			AlertDialog dialog = builder.create();
			dialog.show();
		} else {
			try {
				mCount = new ArrayList<Integer>();
				JSONArray jsonPosts = mData.getJSONArray("materias");
				ArrayList<String> blogPosts = new ArrayList<String>();
				for (int i = 0;i< jsonPosts.length();i++){
					JSONObject post = jsonPosts.getJSONObject(i);
					String title = post.getString("nombre_materia");
					title  = Html.fromHtml(title).toString();
					String periodo = post.getString("periodo");
					periodo = Html.fromHtml(periodo).toString();
					blogPosts.add(title+"_"+periodo);
					mCount.add(post.getJSONArray("componetes").length());
				}
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, blogPosts);
			    name.setAdapter(adapter);
			    if(adapter.getCount() > 0) {
			    	name.setSelection(0);
			    }			    
				save.setEnabled(true);
			} catch (JSONException e) {
				Log.e(TAG,"Exception caught!",e);
			}
		}
	}
	
}
