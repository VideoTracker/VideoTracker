package com.udem.videotracker;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ShareActionProvider;
import android.widget.TextView;


public class VideoActivity extends Activity {
	private Video video;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		video = new Video("Un chat mange un oiseau", "le titre est déjà assez explicite non?\n\n\n\n","www.youtube.com","catLoveeeer",true,1000);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_video);
		
		TextView video_titre = (TextView)findViewById(R.id.video_titre);
		video_titre.setText(video.getTitre());
		TextView video_url = (TextView)findViewById(R.id.video_url);
		video_url.setText(video.getUrl());
		TextView video_description = (TextView)findViewById(R.id.video_description_complet);
		video_description.setText(video.getDescription());
		TextView video_auteur = (TextView)findViewById(R.id.video_auteur);
		video_auteur.setText(video.getAuteur());
		TextView video_nbVues = (TextView)findViewById(R.id.video_nbVues);
		video_nbVues.setText(""+video.getNbVues());
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_video, menu);
		
		// ShareActionProvider
		MenuItem itemProvider = menu.findItem(R.id.menu_share);
		ShareActionProvider mShareActionProvider = (ShareActionProvider) itemProvider.getActionProvider();

		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, video.getUrl());
		mShareActionProvider.setShareIntent(intent);
		
		return true;
	}
	
	public void onCheckboxClicked(View view) {
	    boolean checked = ((CheckBox) view).isChecked();
	    
	    switch(view.getId()) {
	        case R.id.checkbox_fav:
	            if (checked){
	            	video.setFavori(true);
	            	//TODO:AJOUTER AUX FAVORIS
	            }
	            else{
	            	video.setFavori(false);
	            	//TODO:RETIRER DES FAVORIS
	    		}break;
	        
	    }
	}


	@Override
	public boolean onOptionsItemSelected (MenuItem item)
	{
	  Intent intent = null;
	  switch(item.getItemId())
	  {
	    case R.id.tri_alpha:
	    	
	          return true;
	    case android.R.id.home:
	    	this.finish();
	    	  return true;
	    case R.id.tri_nombre:
	    	
	          return true;
	    case R.id.tri_date:
	    	
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
	
}
