package com.udem.videotracker;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;

public class RechercheActivity extends Activity implements OnClickListener {
		private EditText text_search;
		private ImageButton button_search;
		final String SEARCH = "search";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recherche);
		text_search = (EditText) findViewById(R.id.text_search);
		button_search = (ImageButton) findViewById(R.id.button_search);
		button_search.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_recherche, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected (MenuItem item)
	{
	  Intent intent = null;
	  switch(item.getItemId())
	  {
	    case R.id.menu_playlist:
	    	intent = new Intent(RechercheActivity.this,
					PlaylistActivity.class);
			startActivity(intent);
	          return true;
	    case R.id.menu_a_propos:
	    	intent = new Intent(RechercheActivity.this,
					ProposActivity.class);
			startActivity(intent);		      
		      return true;
	    case R.id.menu_aide:
	    	intent = new Intent(RechercheActivity.this,
					AideActivity.class);
			startActivity(intent);		      
		      return true;
	    case R.id.menu_preferences:
	    	intent = new Intent(RechercheActivity.this,
					PreferencesActivity.class);
			startActivity(intent);		      
		      return true;
	      
	  }
	  return super.onOptionsItemSelected(item);
	}
	
	public void onClick(View v) {
		if (v==button_search){
		String test="";
		test+=text_search.getText().toString();
		Intent intent = new Intent(RechercheActivity.this,
				VideoActivity.class);
		intent.putExtra(SEARCH, test);
		}
	}

}
