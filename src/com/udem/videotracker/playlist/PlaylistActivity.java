package com.udem.videotracker.playlist;

import java.util.ArrayList;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

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
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

			alertDialogBuilder.setTitle("Supprimer une playlist");

			alertDialogBuilder
			.setMessage("Êtes vous sûr de vouloir supprimer cette playlist?")
			.setCancelable(false)
			.setPositiveButton("Oui",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					String title = PlaylistData.get(arg2).title;
					PlaylistData.remove(arg2);
					mainList.setAdapter(mainAdapter);
					bdd.open();
					bdd.deletePlaylist(title);
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
			return true;
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

		return true;
	}


	@Override
	public boolean onOptionsItemSelected (MenuItem item)
	{
		Intent intent = null;
		switch(item.getItemId())
		{
		case android.R.id.home:
			this.finish();
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
