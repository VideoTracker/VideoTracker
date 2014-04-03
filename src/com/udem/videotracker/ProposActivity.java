package com.udem.videotracker;

import android.os.Bundle;
import android.view.MenuItem;
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
	
	@Override
	public boolean onOptionsItemSelected (MenuItem item)
	{
	  switch(item.getItemId())
	  {
	    case android.R.id.home:
	    	this.finish();
	    	  return true;
	  }
	  return super.onOptionsItemSelected(item);
	}
}