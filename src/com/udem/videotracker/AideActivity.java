package com.udem.videotracker;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

public class AideActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_aide);
	}

}