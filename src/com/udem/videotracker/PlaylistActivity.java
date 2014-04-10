package com.udem.videotracker;

import java.util.ArrayList;


import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SearchView.OnQueryTextListener;

/**
 * Activité qui va afficher la liste des playlists
 * l'affichage se base sur le playlistAdapter
 * @author rpiche
 *
 */
public class PlaylistActivity extends Activity {

	private ListView mainList;
	private PlaylistAdapter mainAdapter;
	private ArrayList<PlaylistAdapter.PlaylistData> PlaylistData;
	
	
	private class PlaylistListOnItemClick implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> adapter, View view,
				int position, long id) {
			Intent intent = new Intent(PlaylistActivity.this,
					PlaylistVideoActivity.class);
			intent.putExtra("videos", PlaylistData.get(position).id);
			startActivity(intent);		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_playlist);
		
		PlaylistData = new ArrayList<PlaylistAdapter.PlaylistData>();
		//TODO
		//remplir la liste de playlist selon la bdd
		mainAdapter = new PlaylistAdapter(getApplicationContext(), PlaylistData);

		mainList = (ListView) findViewById(R.id.playlistList);
		mainList.setAdapter(mainAdapter);
		mainList.setOnItemClickListener(new PlaylistListOnItemClick());
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_playlists, menu);
		
		MenuItem itemSearch = menu.findItem(R.id.menu_search);
		SearchView mSearchView = (SearchView) itemSearch.getActionView();
		mSearchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				ArrayList<PlaylistAdapter.PlaylistData> res = new ArrayList<PlaylistAdapter.PlaylistData>();
				for (PlaylistAdapter.PlaylistData s : PlaylistData){
					if(s.title.indexOf(query)!=-1)
						res.add(s);
				}
				mainList.setAdapter(new PlaylistAdapter(getApplicationContext(), res));
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}
		});
		
		return true;
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
	    	intent = new Intent(PlaylistActivity.this,
					ProposActivity.class);
			startActivity(intent);		      
		      return true;
	    case R.id.menu_aide:
	    	intent = new Intent(PlaylistActivity.this,
					AideActivity.class);
			startActivity(intent);		      
		      return true;
	    case R.id.menu_preferences:
	    	intent = new Intent(PlaylistActivity.this,
					PreferencesActivity.class);
			startActivity(intent);		      
		      return true;
	      
	  }
	  return super.onOptionsItemSelected(item);
	}
	
}
