package com.udem.videotracker;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity extends Activity {

	private String keywords = "";
	private TextView search_txt = null;
	private String erreur;
	private ListView mainList;
	private VideoAdapter mainAdapter;
	private ArrayList<VideoAdapter.VideoData> weatherData;

	private class VideoListOnItemClick implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> adapter, View view,
				int position, long id) {
			Toast.makeText(getApplicationContext(), "Position " + position,
					Toast.LENGTH_SHORT).show();
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
			}
		} else {
			keywords = (String) savedInstanceState.getSerializable("SEARCH");
		}

		search_txt = (TextView) findViewById(R.id.search_query);
		search_txt.setText("Recherche : " + keywords);

		weatherData = new ArrayList<VideoAdapter.VideoData>();
		
		new DownloadLoginTask().execute();
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {
		case R.id.menu_playlist:
			intent = new Intent(ResultActivity.this,
					PlaylistActivity.class);
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
			intent = new Intent(ResultActivity.this,
					PreferencesActivity.class);
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

	private class DownloadLoginTask extends
			AsyncTask<String, String, YoutubeAPI> {
		/*
		 * Cette m�thode s'ex�cute dans le thread de l'interface. C'est le bon
		 * endroit pour notifier l'usager qu'une t�che plus longue commence (par
		 * exemple, afficher une barre de progression).
		 */
		protected void onPreExecute() {
			// Affiche la barre de progression
			setProgressBarIndeterminateVisibility(true);
		}

		/*
		 * Cette m�thode est ex�cut�e dans son propre thread. C'est l� o� le
		 * travail le plus lourd se passe. On pourra appeler publishProgress
		 * durant l'ex�cution de cette m�thode pour mettre � jour le thread
		 * d'interface.
		 */
		protected YoutubeAPI doInBackground(String... params) {
			YoutubeAPI web = new YoutubeAPI(keywords);
			return web;
		}

		/*
		 * Cette m�thode est appel�e dans le thread d'interface lorsque
		 * publishProgress est appel�e dans doInBackground. Les param�tres sont
		 * pass�s directement de l'une � l'autre.
		 */
		protected void onProgressUpdate(String... s) {
			// �x�cute dans le thread interface, si le thread non-interface
			// appelle publishProgress � l'int�rieur de doInBackground
		}

		/*
		 * Cette m�thode s'ex�cute dans le thread d'interface. C'est l'endroit
		 * o� on r�agit g�n�ralement � la compl�tion du processus en
		 * arri�re-plan, par exemple en mettant � jour l'interface avec les
		 * donn�es obtenues.
		 */
		protected void onPostExecute(YoutubeAPI web) {
			// Cache la barre de progression
			setProgressBarIndeterminateVisibility(false);

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

			weatherData = web.weatherData;
			mainAdapter = new VideoAdapter(getApplicationContext(), weatherData);

			mainList = (ListView) findViewById(R.id.videoList);
			mainList.setAdapter(mainAdapter);
			mainList.setOnItemClickListener(new VideoListOnItemClick());
			search_txt.setText("Nombre de resultats : " + web.nombre_resultats);
		}
	}

	public void maj(String query) {
		this.search_txt.setText(query);

	}

}
