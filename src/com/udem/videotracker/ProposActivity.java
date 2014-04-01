package com.udem.videotracker;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;

public class ProposActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_propos);
	}
}