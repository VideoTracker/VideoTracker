package com.udem.videotracker.playlist;

import java.util.ArrayList;

import com.udem.videotracker.AideActivity;
import com.udem.videotracker.PreferencesActivity;
import com.udem.videotracker.ProposActivity;
import com.udem.videotracker.R;
import com.udem.videotracker.database.VTBDD;
import com.udem.videotracker.recherche.VideoActivity;
import com.udem.videotracker.recherche.VideoAdapter;
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
 * Activit√© qui va afficher la liste des vid√©os au sein d'une playlist
 * l'affichage se base sur le videoAdapter aussi utilis√© pour l'affichage
 * de la liste de vid√©os lors d'une recherche
 * @author rpiche
 *
 */
public class PlaylistVideoActivity extends Activity {

	private ListView mainList;
	private VideoAdapter mainAdapter;
	private ArrayList<VideoAdapter.VideoData> videoData;
	private final Context context = this;
	private VTBDD bdd;
	private int id_playlist;


	private class VideoListOnItemClick implements OnItemClickListener, OnItemLongClickListener {
		@Override
		public void onItemClick(AdapterView<?> adapter, View view,
				int position, long id) {
			Intent intent = new Intent(PlaylistVideoActivity.this,
					VideoActivity.class);
			intent.putExtra("video", videoData.get(position));
			startActivity(intent);
		}

		@Override
		public boolean onItemLongClick(AdapterView<?> adapter, View view,
				int position, long id) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

			alertDialogBuilder.setTitle("Supprimer une playlist");

			alertDialogBuilder
			.setMessage(" tes vous s˚r de vouloir supprimer cette vidÈo de votre playlist?")
			.setCancelable(false)
			.setPositiveButton("Oui",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					bdd.open();
					//bdd.deleteVideoFromPlaylist(videoData.get(id)., id_playlist)
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
		setContentView(R.layout.list_video);
		
		bdd = new VTBDD(this);

		id_playlist = getIntent().getExtras().getInt("ID_PLAYLIST");

		videoData = new ArrayList<VideoAdapter.VideoData>();
		bdd.open();
		videoData = bdd.getPlaylistContent(id_playlist);
		bdd.close();
		mainAdapter = new VideoAdapter(getApplicationContext(), videoData);

		mainList = (ListView) findViewById(R.id.videosList);
		mainList.setAdapter(mainAdapter);
		mainList.setOnItemClickListener(new VideoListOnItemClick());
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_videos, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected (MenuItem item)
	{
		Intent intent = null;
		switch(item.getItemId())
		{
		case R.id.tri_alpha:
			Log.i("TEST","alpha");
			return true;
		case android.R.id.home:
			this.finish();
			return true;
		case R.id.tri_nombre:
			Log.i("TEST","nombre");

			return true;
		case R.id.tri_date:
			Log.i("TEST","date");

			return true;	

		case R.id.menu_a_propos:
			intent = new Intent(PlaylistVideoActivity.this,
					ProposActivity.class);
			startActivity(intent);		      
			return true;
		case R.id.menu_aide:
			intent = new Intent(PlaylistVideoActivity.this,
					AideActivity.class);
			startActivity(intent);		      
			return true;
		case R.id.menu_preferences:
			intent = new Intent(PlaylistVideoActivity.this,
					PreferencesActivity.class);
			startActivity(intent);		      
			return true;

		}
		return super.onOptionsItemSelected(item);
	}

}
