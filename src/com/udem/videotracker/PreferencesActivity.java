package com.udem.videotracker;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

/**
 * Activité qui va permettre à l'utilisateur de gérer
 * ses préférences concernant l'application
 * @author rpiche
 *
 */
public class PreferencesActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_pref);
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