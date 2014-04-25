package com.udem.videotracker;

import java.util.ArrayList;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activit√© principale de l'application, elle est lanc√©e
 * au d√©marrage. Elle permet de faire une recherche selon
 * les checkbox coch√©es.
 * @author rpiche
 *
 */
public class RechercheActivity extends Activity implements OnClickListener {
	private EditText text_search;
	private ImageButton button_search;
	private ImageButton button_voice;
	private static final int VOICE_RECOGNITION_REQUEST = 0x10101;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recherche);
		text_search = (EditText) findViewById(R.id.text_search);
		button_search = (ImageButton) findViewById(R.id.button_search);
		button_search.setOnClickListener(this);
		button_voice = (ImageButton) findViewById(R.id.button_voice);
		button_voice.setOnClickListener(this);
		
		// 1. Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		// 2. Chain together various setter methods to set the dialog characteristics
		builder.setMessage("coucou")
		       .setTitle("titre");

		// 3. Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		
		VTBDD db = new VTBDD(this);
	    db.open();
	    db.addPlaylist("playlist", "2014-12-12");
		
	}
	
	public void speakToMe() {
	    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
	    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
	        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
	    intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
	        "Veuillez donner de mots-clÈs concernant la vidÈo ‡ haute voix.");
	    startActivityForResult(intent, VOICE_RECOGNITION_REQUEST);
	  }
	
	  @Override
	  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == VOICE_RECOGNITION_REQUEST && resultCode == RESULT_OK) {
	      ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
	      String firstMatch = matches.get(0);
	      text_search.setText(firstMatch);
	    }
	  }

	@Override
	protected void onStart() {
		super.onStart();

		if(!isOnline(this)){
			Intent intent = new Intent(RechercheActivity.this,
					InternetCheckActivity.class);
			startActivity(intent);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_recherche, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {
		case R.id.menu_playlist:
			intent = new Intent(RechercheActivity.this, PlaylistActivity.class);
			startActivity(intent);
			return true;
		case R.id.menu_a_propos:
			intent = new Intent(RechercheActivity.this, ProposActivity.class);
			startActivity(intent);
			return true;
		case R.id.menu_aide:
			intent = new Intent(RechercheActivity.this, VideoActivity.class);
			startActivity(intent);
			return true;
		case R.id.menu_preferences:
			intent = new Intent(RechercheActivity.this,
					PreferencesActivity.class);
			startActivity(intent);
			return true;

		}
		return super.onOptionsItemSelected(item);
	}

	public void onClick(View v) {
		if (v == button_search) {
			String search = "";
			CheckBox checkD = (CheckBox) findViewById(R.id.checkbox_dailymotion);
			CheckBox checkY = (CheckBox) findViewById(R.id.checkbox_youtube);
			if ((!checkD.isChecked()) && (!checkY.isChecked())) {
				Toast.makeText(
						getApplicationContext(),
						"Veuillez sÈlectionner au moins un moteur de recherche",
						Toast.LENGTH_SHORT).show();
			}
			search += text_search.getText().toString();
			Intent intent = new Intent(RechercheActivity.this,
					ResultActivity.class);
			intent.putExtra("SEARCH", search);
			intent.putExtra("SEARCH_DAILYMOTION", checkD.isChecked());
			intent.putExtra("SEARCH_YOUTUBE", checkY.isChecked());
			startActivity(intent);
		}
		if (v == button_voice) {
			speakToMe();
		}
	}

	public static boolean isOnline(Activity a) {
		ConnectivityManager cm =
				(ConnectivityManager) a.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

}
