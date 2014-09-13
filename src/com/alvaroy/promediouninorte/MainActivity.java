package com.alvaroy.promediouninorte;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class MainActivity extends ActionBarActivity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.RED));
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.main, new Master()).commit();
		}
	}
	
}
