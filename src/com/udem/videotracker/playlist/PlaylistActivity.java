package com.udem.videotracker.playlist;

import java.util.ArrayList;
import java.util.Date;

import com.udem.videotracker.AideActivity;
import com.udem.videotracker.PreferencesActivity;
import com.udem.videotracker.ProposActivity;
import com.udem.videotracker.R;
import com.udem.videotracker.database.VTBDD;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SearchView.OnQueryTextListener;

/**
 * ActivitÃ© qui va afficher la liste des playlists
 * l'affichage se base sur le playlistAdapter
 * @author rpiche
 *
 */
public class PlaylistActivity extends Activity {

	private ListView mainList;
	private PlaylistAdapter mainAdapter;
	private ArrayList<PlaylistAdapter.PlaylistData> PlaylistData;
	private final Context context = this;
	private VTBDD bdd;


	private class PlaylistListOnItemClick implements OnItemClickListener, OnItemLongClickListener {
		@Override
		public void onItemClick(AdapterView<?> adapter, View view,
				int position, long id) {
			Intent intent = new Intent(PlaylistActivity.this,
					PlaylistVideoActivity.class);
			intent.putExtra("PLAYLIST_ID", PlaylistData.get(position).id);
			startActivity(intent);		}
		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

			alertDialogBuilder.setTitle("Supprimer une playlist");

			alertDialogBuilder
			.setMessage("Êtes vous sûr de vouloir supprimer cette playlist?")
			.setCancelable(false)
			.setPositiveButton("Oui",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					bdd.open();
					bdd.deletePlaylist(PlaylistData.get(id).title);
					bdd.close();
				}
			})
			.setNegativeButton("Non",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					dialog.cancel();
				}
			});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
			return false;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_playlist);

		bdd = new VTBDD(this);
		bdd.open();
		PlaylistData = bdd.getPlaylists();
		bdd.close();
		mainAdapter = new PlaylistAdapter(getApplicationContext(), PlaylistData);

		mainList = (ListView) findViewById(R.id.playlistList);
		mainList.setAdapter(mainAdapter);
		mainList.setOnItemClickListener(new PlaylistListOnItemClick());
		mainList.setOnItemLongClickListener(new PlaylistListOnItemClick());
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
				ArrayList<PlaylistAdapter.PlaylistData> tmp = new ArrayList<PlaylistAdapter.PlaylistData>(PlaylistData);
				for (PlaylistAdapter.PlaylistData s : tmp){
					if(s.title.indexOf(query)==-1)
						PlaylistData.remove(s);
				}
				mainAdapter.notifyDataSetChanged();
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
			//PlaylistData = getAlpha();
			mainAdapter.notifyDataSetChanged();
			return true;
		case android.R.id.home:
			this.finish();
			return true;
		case R.id.tri_nombre:
			//PlaylistData = getNombre();
			mainAdapter.notifyDataSetChanged();
			return true;
		case R.id.tri_date:
			//PlaylistData = getDate();
			mainAdapter.notifyDataSetChanged();
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
