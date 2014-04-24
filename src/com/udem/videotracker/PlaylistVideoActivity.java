package com.udem.videotracker;

import java.util.ArrayList;
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
 * Activité qui va afficher la liste des vidéos au sein d'une playlist
 * l'affichage se base sur le videoAdapter aussi utilisé pour l'affichage
 * de la liste de vidéos lors d'une recherche
 * @author rpiche
 *
 */
public class PlaylistVideoActivity extends Activity {

	private ListView mainList;
	private VideoAdapter mainAdapter;
	private ArrayList<VideoAdapter.VideoData> videoData;
	private final Context context = this;



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
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

			alertDialogBuilder.setTitle("Supprimer une playlist");

			alertDialogBuilder
			.setMessage("�tes vous s�r de vouloir supprimer cette vid�o de votre playlist?")
			.setCancelable(false)
			.setPositiveButton("Oui",new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int id) {
					//TODO suppression video de la playlist
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
		setContentView(R.layout.activity_resultat_video);

		//int id_playlist = getIntent().getExtras().getParcelable("videos");

		videoData = new ArrayList<VideoAdapter.VideoData>();
		//TODO
		//remplir la liste de video selon la playlist avec l'id recuperé
		//test
		//videoData.add(new VideoAdapter.VideoData("titre", "description \n\n\n\n\n\n", "auteur", "url", true,false,25000,getResources().getDrawable(R.drawable.ic_content_remove)));
		mainAdapter = new VideoAdapter(getApplicationContext(), videoData);

		mainList = (ListView) findViewById(R.id.videoList);
		mainList.setAdapter(mainAdapter);
		mainList.setOnItemClickListener(new VideoListOnItemClick());
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_videos, menu);

		MenuItem itemSearch = menu.findItem(R.id.menu_search);
		SearchView mSearchView = (SearchView) itemSearch.getActionView();
		mSearchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				//TODO recherche de mots clés dans les vidéos d'une playlist par rapport au titre
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
