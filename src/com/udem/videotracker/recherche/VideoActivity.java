package com.udem.videotracker.recherche;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import com.udem.videotracker.AideActivity;
import com.udem.videotracker.PreferencesActivity;
import com.udem.videotracker.ProposActivity;
import com.udem.videotracker.R;
import com.udem.videotracker.api.VideoAPI;
import com.udem.videotracker.database.DBHelperVT;
import com.udem.videotracker.database.VTBDD;
import com.udem.videotracker.playlist.PlaylistAdapter;
import com.udem.videotracker.recherche.VideoAdapter.Source;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.app.AlertDialog;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Activité qui affiche une vidéo ainsi que ses infos.
 * La vidéo sera lancée à  partir de cette page.
 * @author rpiche
 *
 */
public class VideoActivity extends Activity{
	private String videoId;
	public String url;
	private Source source;
	public TextView video_titre;
	public TextView video_url;
	public TextView video_description;
	public TextView video_nbVues;
	public TextView video_like_count;
	public CheckBox favori;
	public VideoAdapter.VideoData video;
	public ImageView image;
	public ImageButton button_play;
	public VTBDD bdd;
	public ArrayList<PlaylistAdapter.PlaylistData> playlists;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_video);

		Bundle extras = getIntent().getExtras();
		if(extras == null){
			return;
		}
		videoId = extras.getString("video");
		source = (Source)getIntent().getSerializableExtra("source");
		new LoadVideo(this).execute();

		bdd = new VTBDD(this);

		video_titre = (TextView)findViewById(R.id.video_titre);

		video_url = (TextView)findViewById(R.id.video_url);

		video_description = (TextView)findViewById(R.id.video_description_complet);

		video_nbVues = (TextView)findViewById(R.id.video_nbVues);

		video_like_count = (TextView)findViewById(R.id.video_like_count);

		image = (ImageView)findViewById(R.id.video_image);

		favori = (CheckBox) findViewById(R.id.checkbox_fav);

		button_play = (ImageButton) findViewById(R.id.button_play);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_video, menu);
		MenuItem itemProvider = menu.findItem(R.id.menu_share);
		ShareActionProvider mShareActionProvider = (ShareActionProvider) itemProvider.getActionProvider();

		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		switch (source){
		case YOUTUBE :
			intent.putExtra(Intent.EXTRA_TEXT, "Regarde cette vidéo : https://m.youtube.com/watch?v="+videoId);
			break;
		case DAILYMOTION :
			intent.putExtra(Intent.EXTRA_TEXT, "Regarde cette vidéo : http://www.dailymotion.com/video/"+videoId);
			break;	
		}

		mShareActionProvider.setShareIntent(intent);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected (MenuItem item)
	{
		Intent intent = null;
		switch(item.getItemId())
		{
		case R.id.menu_a_propos:
			intent = new Intent(VideoActivity.this,
					ProposActivity.class);
			startActivity(intent);		      
			return true;
		case R.id.menu_add:
			AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
			builderSingle.setTitle("Dans quelle playlist ? -");
			final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
					android.R.layout.select_dialog_singlechoice);
			playlists = new ArrayList<PlaylistAdapter.PlaylistData>();
			bdd.open();
			playlists=bdd.getPlaylists();
			bdd.close();
			for (PlaylistAdapter.PlaylistData pl : playlists){
				arrayAdapter.add(pl.title);
			}
			arrayAdapter.add("Ajouter une nouvelle playlist");
			builderSingle.setNegativeButton("cancel",
					new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});

			builderSingle.setAdapter(arrayAdapter,
					new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int id) {
					if(id == playlists.size()){
						final EditText input = new EditText(VideoActivity.this);
						new AlertDialog.Builder(VideoActivity.this)
						.setTitle("Nom de la nouvelle playlist")
						.setView(input)
						.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								Editable value = input.getText();
								bdd.open();
								bdd.addPlaylist(value.toString(), new Date().toString());
								playlists=bdd.getPlaylists();
								bdd.addVideoToPlaylist(video, playlists.get(playlists.size()).id);
								bdd.close();
							}
						}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
							}
						}).show();
					}
					else{
						bdd.open();
						Log.i("toto", ""+playlists.get(id).id);
						bdd.addVideoToPlaylist(video, playlists.get(id).id);
						bdd.close();
					}
				}
			});
			builderSingle.show();

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
		case android.R.id.home:
			this.finish();
			return true;

		}
		return super.onOptionsItemSelected(item);
	}




	private class LoadVideo extends AsyncTask<String, Integer, VideoAPI>{

		public VideoActivity activity;

		public LoadVideo(VideoActivity a)
		{
			this.activity = a;
		}



		protected void onPostExecute(VideoAPI web) {

			if (web == null) {
				Toast.makeText(VideoActivity.this, "erreur fatale",
						Toast.LENGTH_SHORT).show();
				return;
			}

			if (web.erreur != null) {
				Toast.makeText(VideoActivity.this, web.erreur,
						Toast.LENGTH_SHORT).show();
				return;
			}
		}
		@Override
		protected VideoAPI doInBackground(String... params) {
			VideoAPI web = null;
			try {
				web = new VideoAPI(activity, videoId,
						source);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return web;

		}

	}
}
