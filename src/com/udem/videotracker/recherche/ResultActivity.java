package com.udem.videotracker.recherche;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.udem.videotracker.AideActivity;
import com.udem.videotracker.InternetCheckActivity;
import com.udem.videotracker.PreferencesActivity;
import com.udem.videotracker.ProposActivity;
import com.udem.videotracker.R;
import com.udem.videotracker.api.YoutubeAPI;
import com.udem.videotracker.playlist.PlaylistActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity extends Activity {

	private String keywords = "";
	private TextView search_txt = null;
	private ListView mainList;
	private VideoAdapter mainAdapter;
	private ArrayList<VideoAdapter.VideoData> videoData;
	private ProgressBar progressBar;
	private boolean searchDailymotion = false;
	private boolean searchYoutube = false;

	private class VideoListOnItemClick implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> adapter, View view,
				int position, long id) {
			Intent intent = new Intent(ResultActivity.this, VideoActivity.class);
			intent.putExtra("video", videoData.get(position).url_video);
			intent.putExtra("source", videoData.get(position).sourceVideo);
			startActivity(intent);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_resultat_video);

		Bundle extras;
		if (savedInstanceState == null) {
			extras = getIntent().getExtras();
			if (extras == null) {
				keywords = null;
			} else {
				keywords = extras.getString("SEARCH");

				// retirer les espaces superflus au debut et a la fin
				keywords = keywords.trim();

				// remplacer les espaces dans le mot par des +
				keywords = keywords.replaceAll("\\s+", "+");

				searchDailymotion = extras.getBoolean("SEARCH_DAILYMOTION");
				searchYoutube = extras.getBoolean("SEARCH_YOUTUBE");
			}
		} else {
			keywords = (String) savedInstanceState.getSerializable("SEARCH");
		}

		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		progressBar.setProgress(0);
		progressBar.setVisibility(View.VISIBLE);

		search_txt = (TextView) findViewById(R.id.search_query);
		search_txt.setText("Recherche : " + keywords);

		videoData = new ArrayList<VideoAdapter.VideoData>();
		mainAdapter = new VideoAdapter(getApplicationContext(), videoData);

		mainList = (ListView) findViewById(R.id.videoList);
		mainList.setAdapter(mainAdapter);
		mainList.setOnItemClickListener(new VideoListOnItemClick());

		if (!RechercheActivity.isOnline(this)) {
			Intent intent = new Intent(ResultActivity.this,
					InternetCheckActivity.class);
			startActivity(intent);
		}

		new SearchVideos(this).execute();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {
		case R.id.tri_alpha: {
			Comparator<VideoAdapter.VideoData> myComparator = new Comparator<VideoAdapter.VideoData>() {
				public int compare(VideoAdapter.VideoData obj1,
						VideoAdapter.VideoData obj2) {
					return obj1.title.compareTo(obj2.title);
				}
			};
			Collections.sort(videoData, myComparator);
			mainAdapter.notifyDataSetChanged();
			return true;
		}
		case android.R.id.home:
			this.finish();
			return true;
		case R.id.tri_source: {
			Comparator<VideoAdapter.VideoData> myComparator = new Comparator<VideoAdapter.VideoData>() {
				public int compare(VideoAdapter.VideoData obj1,
						VideoAdapter.VideoData obj2) {
					return obj1.sourceVideo.compareTo(obj2.sourceVideo);
				}
			};
			Collections.sort(videoData, myComparator);
			mainAdapter.notifyDataSetChanged();
			return true;
		}
		case R.id.tri_date: {
			Comparator<VideoAdapter.VideoData> myComparator = new Comparator<VideoAdapter.VideoData>() {
				public int compare(VideoAdapter.VideoData obj1,
						VideoAdapter.VideoData obj2) {
					return obj1.datePublication.compareTo(obj2.datePublication);
				}
			};
			Collections.sort(videoData, myComparator);
			mainAdapter.notifyDataSetChanged();
			return true;
		}
		case R.id.menu_playlist:
			intent = new Intent(ResultActivity.this, PlaylistActivity.class);
			startActivity(intent);
			return true;
		case R.id.menu_a_propos:
			intent = new Intent(ResultActivity.this, ProposActivity.class);
			startActivity(intent);
			return true;
		case R.id.menu_aide:
			intent = new Intent(ResultActivity.this, AideActivity.class);
			startActivity(intent);
			return true;
		case R.id.menu_preferences:
			intent = new Intent(ResultActivity.this, PreferencesActivity.class);
			startActivity(intent);
			return true;

		}
		return super.onOptionsItemSelected(item);
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
				maj(query);
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}
		});

		return true;
	}

	private class SearchVideos extends AsyncTask<String, Integer, YoutubeAPI> {

		public ResultActivity activity;

		public SearchVideos(ResultActivity a) {
			this.activity = a;
		}

		protected YoutubeAPI doInBackground(String... params) {
			YoutubeAPI web = null;
			try {
				web = new YoutubeAPI(activity, keywords, progressBar,
						searchDailymotion, searchYoutube, videoData,
						mainAdapter);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return web;
		}

		protected void onProgressUpdate(Integer... pourcentage) {

			super.onProgressUpdate(pourcentage);
			progressBar.setProgress(pourcentage[0]);
		}

		protected void onPostExecute(YoutubeAPI web) {
			// Cache la barre de progression
			progressBar.setProgress(100);
			progressBar.setVisibility(View.GONE);

			// On s'assure que l'objet de retour existe
			// et qu'il n'ait pas d'erreurs
			if (web == null) {
				Toast.makeText(ResultActivity.this, "erreur fatale",
						Toast.LENGTH_SHORT).show();
				return;
			}

			if (web.erreur != null) {
				Toast.makeText(ResultActivity.this, web.erreur,
						Toast.LENGTH_SHORT).show();
				return;
			}

			search_txt.setText("Nombre de resultats : " + videoData.size());

			mainAdapter.notifyDataSetChanged();
		}
	}

	public void maj(String query) {
		this.search_txt.setText(query);

	}

}