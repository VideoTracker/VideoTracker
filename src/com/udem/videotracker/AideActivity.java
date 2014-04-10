package com.udem.videotracker;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class AideActivity extends Activity{


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_aide);

	}
	
	public void onCheckboxClicked(View view) {
		TextView toto = (TextView) findViewById(R.id.test);
	    // Is the view now checked?
	    boolean checked = ((CheckBox) view).isChecked();
	    
	    // Check which checkbox was clicked
	    switch(view.getId()) {
	        case R.id.checkbox_fav:
	            if (checked)
	            	toto.setText("True");
	            else
	                toto.setText("False");
	            break;
	        
	    }
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