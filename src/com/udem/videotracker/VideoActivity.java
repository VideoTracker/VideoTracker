package com.udem.videotracker;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class VideoActivity extends Activity {
	
	final String SEARCH = "search";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_video);
		
       Intent intent = getIntent();
       TextView search = (TextView) findViewById(R.id.search_query);
       if (intent != null) {
    	   search.setText(intent.getStringExtra(SEARCH));
    	   
       }
	}

	
	@Override
	public boolean onOptionsItemSelected (MenuItem item)
	{
	  Intent intent = null;
	  switch(item.getItemId())
	  {
	    case R.id.menu_playlist:
	    	intent = new Intent(VideoActivity.this,
					PlaylistActivity.class);
			startActivity(intent);
	          return true;
	    case R.id.menu_a_propos:
	    	intent = new Intent(VideoActivity.this,
					ProposActivity.class);
			startActivity(intent);		      
		      return true;
	    case R.id.menu_aide:
	    	intent = new Intent(VideoActivity.this,
					AideActivity.class);
			startActivity(intent);		      
		      return true;
	    case R.id.menu_preferences:
	    	intent = new Intent(VideoActivity.this,
					PreferencesActivity.class);
			startActivity(intent);		      
		      return true;
	      
	  }
	  return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_videos, menu);
		return true;
	}

}
